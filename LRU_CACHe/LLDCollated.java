// CacheEntry.java

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



// LRUCache.java

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



// Main.java

package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("🧠 LRU Cache: The Brain with 3-Item Memory! 🧠");
        System.out.println("=============================================");
        
        LRUCache<String, String> brain = new LRUCache<>(3);
        
        System.out.println("\n📚 Teaching the brain 3 things:");
        brain.put("Pizza", "🍕 Delicious");
        brain.put("Coffee", "☕ Life fuel");
        brain.put("Sleep", "😴 What's that?");
        System.out.println("Brain learned: Pizza, Coffee, Sleep (3/3 memory slots used)");
        
        System.out.println("\n🔍 Testing memory recall (refreshes memory):");
        System.out.println("Remember Pizza? " + brain.get("Pizza") + " ✅ (Pizza is now fresh in mind!)");
        System.out.println("Remember Coffee? " + brain.get("Coffee") + " ✅ (Coffee is now fresh in mind!)");
        System.out.println("Remember Sleep? " + brain.get("Sleep") + " ✅ (Sleep is now fresh in mind!)");
        
        System.out.println("\n🆘 Brain overload! Adding 4th thing:");
        System.out.println("⚠️  Brain is full! Oldest memory (Pizza) will be forgotten...");
        brain.put("Deadline", "🔥 Tomorrow!");
        System.out.println("Brain learned: Deadline");
        System.out.println("Remember Pizza? " + brain.get("Pizza") + " ❌ (Forgotten! Brain needed space)");
        System.out.println("Remember Coffee? " + brain.get("Coffee") + " ✅ (Still remembers!)");
        System.out.println("Remember Sleep? " + brain.get("Sleep") + " ✅ (Still remembers!)");
        System.out.println("Remember Deadline? " + brain.get("Deadline") + " ✅ (New memory!)");
        
        System.out.println("\n🔄 Updating a memory:");
        System.out.println("Before: Coffee = " + brain.get("Coffee"));
        brain.put("Coffee", "☕ EXTRA STRONG!");
        System.out.println("After: Coffee = " + brain.get("Coffee") + " 💪 (Upgraded and refreshed!)");
        
        System.out.println("\n🤯 Brain meltdown mode!");
        brain.put("Netflix", "📺 One more episode...");
        System.out.println("Learned Netflix (forgot Sleep - oldest memory)");
        brain.put("Procrastination", "🕰️ Tomorrow's problem");
        System.out.println("Learned Procrastination (forgot Deadline - oldest memory)");
        System.out.println("Remember Sleep? " + brain.get("Sleep") + " ❌ (Gone forever!)");
        System.out.println("Remember Deadline? " + brain.get("Deadline") + " ❌ (Also gone!)");
        System.out.println("Remember Coffee? " + brain.get("Coffee") + " ✅ (Coffee is immortal!)");
        System.out.println("Remember Netflix? " + brain.get("Netflix") + " ✅ (Obviously)");
        System.out.println("Remember Procrastination? " + brain.get("Procrastination") + " ✅ (Natural talent)");
        
        System.out.println("\n❓ Testing non-existent memory:");
        System.out.println("Remember 'Brain Cells'? " + brain.get("Brain Cells") + " 🤔 (Never heard of it)");
        
        System.out.println("\n🎯 Brain Training Complete!");
        System.out.println("Summary: The brain remembers recent stuff, forgets old stuff!");
        System.out.println("Just like your browser tabs... but with more coffee! ☕");
    }
}


