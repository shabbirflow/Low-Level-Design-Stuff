# 🚦 Rate Limiter — LLD

A pluggable rate limiter supporting multiple algorithms — **Token Bucket** and **Fixed Window Counter** — switchable at construction time via the Strategy pattern. Maintains independent limits per user/API key.

---

## 🧩 Class Design

```
RateLimiter
└── RateLimitPolicy  (interface)
        ├── TokenBucketPolicy
        │       └── Map<key, TokenBucket>
        │               └── TokenBucket  (tokens, lastRefillTimestamp)
        └── FixedWindowPolicy
                └── Map<key, WindowCounter>
                        └── WindowCounter  (count, windowStartTime)
```

### Classes

| Class | Role |
|-------|------|
| `RateLimiter` | Entry point. Holds a `RateLimitPolicy` and delegates every `allowRequest(key)` call to it. |
| `RateLimitPolicy` | Interface: `boolean allowRequest(String key, long currentTimeMillis)` |
| `TokenBucketPolicy` | Token bucket algorithm. Refills tokens continuously over time; allows request if `tokens >= 1`. |
| `TokenBucket` | Per-key state: current token count (double) + last refill timestamp. |
| `FixedWindowPolicy` | Fixed window counter algorithm. Counts requests in a fixed time window; resets on new window. |
| `WindowCounter` | Per-key state: request count + window start timestamp. |

---

## ⚙️ Design Decisions

### Strategy Pattern — Swappable Algorithms
`RateLimiter` depends only on the `RateLimitPolicy` interface. Switching algorithms is a one-line change:
```java
// Token Bucket: 10 requests/sec, burst up to 10
RateLimiter limiter = new RateLimiter(new TokenBucketPolicy(10, 10.0));

// Fixed Window: 5 requests per 1000ms window
RateLimiter limiter = new RateLimiter(new FixedWindowPolicy(5, 1000));
```
No `if/else` chains, no modification to `RateLimiter`.

### Per-Key State
Each policy maintains a `Map<String, State>` — keyed by user ID or API key — so every client has its own independent bucket or counter. New keys are lazily initialised on first request.

### Token Bucket: Continuous Refill
Tokens refill proportionally to elapsed real time:
```
tokensToAdd = elapsedMillis × refillRatePerMillis
bucket.tokens = min(capacity, bucket.tokens + tokensToAdd)
```
This handles bursts gracefully — a quiet client accumulates tokens up to `capacity`, then spends them in a burst.

### Fixed Window: Hard Reset
```
if now > windowStart + windowSize:
    count = 0
    windowStart = now
if count < limit:
    count++; allow
else: reject
```
Simple and predictable, but has a boundary burst problem (known trade-off vs Sliding Window).

---

## 🔄 Request Flows

### Token Bucket
```
allowRequest(key, now)
  1. Get or create TokenBucket for key
  2. refill(bucket, now)
       elapsedMs = now - lastRefillTimestamp
       tokens = min(capacity, tokens + elapsed × rate)
       lastRefillTimestamp = now
  3. if tokens >= 1: tokens -= 1; return true
     else: return false
```

### Fixed Window
```
allowRequest(key, now)
  1. Get or create WindowCounter for key
  2. if now > windowStart + windowSize:
         count = 0; windowStart = now
  3. if count < limit: count++; return true
     else: return false
```

---

## 📊 Algorithm Comparison

| Property | Token Bucket | Fixed Window |
|----------|-------------|--------------|
| Burst handling | ✅ Smooth burst up to capacity | ❌ Boundary burst possible |
| Memory per key | O(1) | O(1) |
| Implementation | Moderate | Simple |
| Use case | API gateways, smooth traffic | Simple quota enforcement |

---

## 🧠 Design Patterns Used

| Pattern | Where |
|---------|-------|
| **Strategy** | `RateLimitPolicy` interface with swappable implementations |
| **Lazy Initialization** | Per-key buckets created on first request |

---

## 📁 Source Files

```
src/main/java/org/example/
├── RateLimiter.java         ← Entry point, delegates to policy
├── RateLimitPolicy.java     ← Strategy interface
├── TokenBucketPolicy.java   ← Token bucket algorithm
├── TokenBucket.java         ← Per-key token bucket state
├── FixedWindowPolicy.java   ← Fixed window algorithm
├── WindowCounter.java       ← Per-key fixed window state
└── Main.java                ← Demo
```

## ▶️ Running

```bash
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```
