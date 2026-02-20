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