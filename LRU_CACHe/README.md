# 🗄️ LRU Cache — LLD

A generic, fixed-capacity **Least Recently Used (LRU) Cache** implemented from scratch using a `HashMap` + custom **Doubly Linked List** — achieving O(1) time complexity for both `get` and `put`.

---

## 🧩 Class Design

```
LRUCache<K, V>
├── HashMap<K, CacheEntry<K,V>>    ← O(1) key lookup
├── CacheEntry<K,V> head           ← sentinel (dummy head)
└── CacheEntry<K,V> tail           ← sentinel (dummy tail)

        head ↔ [MRU] ↔ ... ↔ [LRU] ↔ tail
```

### Classes

| Class | Role |
|-------|------|
| `LRUCache<K, V>` | The cache. Generic over key and value types. Maintains a `HashMap` for O(1) lookup and a doubly linked list for O(1) access-order tracking. |
| `CacheEntry<K, V>` | A node in the doubly linked list. Holds `key`, `value`, `prev`, and `next` pointers. |

---

## ⚙️ Design Decisions

### Why HashMap + Doubly Linked List?
| Requirement | Solution |
|-------------|----------|
| O(1) lookup by key | `HashMap<K, CacheEntry>` |
| O(1) track & update access order | Doubly Linked List (move node to front on access) |
| O(1) evict LRU entry | Always `tail.prev` — remove in O(1) |

A singly linked list would require O(n) to find the previous node. A `LinkedHashMap` could do this too, but implementing it from scratch demonstrates the underlying mechanism.

### Sentinel Nodes
`head` and `tail` are **dummy sentinel nodes** (holding `null` key/value). They permanently anchor the list, eliminating all null checks in pointer manipulation:
```
head ↔ [entry1 (MRU)] ↔ [entry2] ↔ [entry3 (LRU)] ↔ tail
```

### Access-Order Maintenance
On every `get` or `put`, the accessed entry is moved to `head.next` (front = Most Recently Used). Eviction always removes `tail.prev` (back = Least Recently Used).

### Generic Design
`LRUCache<K, V>` works for any key/value types:
```java
LRUCache<Integer, String> cache = new LRUCache<>(3);
LRUCache<String, User>    cache = new LRUCache<>(100);
```

---

## 🔄 Operations

### `get(key)` → O(1)
```
1. cache.get(key) → CacheEntry  (HashMap lookup)
2. moveToFront(entry)           (removeNode + addToFront)
3. return entry.value
```

### `put(key, value)` → O(1)
```
1. cache.get(key)
   → if exists: update value, moveToFront, return
2. if cache.size() == capacity:
       evictLeastRecentlyUsed()   ← remove tail.prev from list + map
3. newEntry = new CacheEntry(key, value)
4. cache.put(key, newEntry)
5. addToFront(newEntry)           ← insert after head
```

### Internal Pointer Operations
```java
removeNode(entry):
    entry.prev.next = entry.next
    entry.next.prev = entry.prev

addToFront(entry):
    entry.next = head.next
    entry.prev = head
    head.next.prev = entry
    head.next = entry
```

---

## 📊 Complexity Summary

| Operation | Time | Space |
|-----------|------|-------|
| `get` | O(1) | — |
| `put` | O(1) | — |
| Overall | — | O(capacity) |

---

## 🧠 Design Patterns Used

| Pattern | Where |
|---------|-------|
| **Doubly Linked List + HashMap** | Core data structure combination |
| **Sentinel Node** | `head` and `tail` dummy nodes simplify edge cases |
| **Generic Programming** | `LRUCache<K,V>` works with any types |

---

## 📁 Source Files

```
src/main/java/org/example/
├── LRUCache.java      ← Core cache: HashMap + DLL logic
├── CacheEntry.java    ← DLL node: key, value, prev, next
└── Main.java          ← Demo with get/put scenarios
```

## ▶️ Running

```bash
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```
