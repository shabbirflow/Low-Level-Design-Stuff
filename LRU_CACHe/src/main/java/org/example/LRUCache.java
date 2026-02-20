package org.example;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, CacheEntry<K, V>> cache;

    private CacheEntry<K, V> head;
    private CacheEntry<K, V> tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        head = new CacheEntry<>(null, null);
        tail = new CacheEntry<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public V get(K key) {
        CacheEntry<K, V> entry = cache.get(key);
        if (entry == null) {
            return null;
        }
        moveToFront(entry);
        return entry.value;
    }

    public void put(K key, V value){
        CacheEntry<K, V> entry = cache.get(key);
        if (entry != null) {
            entry.value = value;
            moveToFront(entry);
            return;
        }

        if(capacity == cache.size()){
            evictLeastRecentlyUsed();
        }

        CacheEntry<K, V> newEntry = new CacheEntry<>(key, value);
        cache.put(key, newEntry);
        addToFront(newEntry);
    }

    private void evictLeastRecentlyUsed() {
        CacheEntry<K, V> lru = tail.prev;
        cache.remove(lru.key);
        removeNode(lru);
    }


    private void moveToFront(CacheEntry<K, V> entry) {
        removeNode(entry);
        addToFront(entry);
    }

    private void removeNode(CacheEntry<K,V> entry) {
        entry.prev.next = entry.next;
        entry.next.prev = entry.prev;
    }

    private void addToFront(CacheEntry<K, V> entry) {
        entry.next = head.next;
        entry.prev = head;

        head.next.prev = entry;
        head.next = entry;
    }


}
