package org.example;

import java.util.HashMap;
import java.util.Map;

class TokenBucketPolicy implements RateLimitPolicy {

    private final int capacity;
    private final double refillRatePerMillis;
    private final Map<String, TokenBucket> buckets = new HashMap<>();

    public TokenBucketPolicy(int capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerMillis = refillRatePerSecond / 1000.0;
    }

    @Override
    public boolean allowRequest(String key, long now) {
        TokenBucket bucket = buckets.get(key);

        if (bucket == null) {
            bucket = new TokenBucket(capacity, now);
            buckets.put(key, bucket);
        }

        refill(bucket, now);

        if (bucket.tokens >= 1) {
            bucket.tokens -= 1;
            return true;
        }

        return false;
    }

    private void refill(TokenBucket bucket, long now) {
        long elapsedMillis = now - bucket.lastRefillTimestamp;
        if (elapsedMillis <= 0) {
            return;
        }

        double tokensToAdd = elapsedMillis * refillRatePerMillis;
        bucket.tokens = Math.min(capacity, bucket.tokens + tokensToAdd);
        bucket.lastRefillTimestamp = now;
    }
}

