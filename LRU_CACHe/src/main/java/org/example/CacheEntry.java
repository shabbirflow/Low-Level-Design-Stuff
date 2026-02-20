package org.example;

public class CacheEntry<K, V> {
    K key;
    V value;
    CacheEntry<K, V> prev;
    CacheEntry<K, V> next;

    public CacheEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
