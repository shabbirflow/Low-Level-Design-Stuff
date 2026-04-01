//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        FileSystem fs = new FileSystem();

        System.out.println("🐒 Monkey Shakespeare enters the jungle...\n");

        // Monkey creates folders
        fs.mkdir("/jungle");
        fs.mkdir("/jungle/banana_stories");
        fs.mkdir("/jungle/banana_stories/drafts");

        System.out.println("🌴 Jungle structure created.");
        System.out.println("ls(/jungle): " + fs.ls("/jungle"));
        System.out.println();

        // Monkey writes masterpiece
        fs.addContentToFile("/jungle/banana_stories/hamlet.txt", "To eat or not to eat banana\n");
        fs.addContentToFile("/jungle/banana_stories/hamlet.txt", "That is the question 🐒\n");

        System.out.println("📖 Monkey writes Hamlet:");
        System.out.println(fs.readContentFromFile("/jungle/banana_stories/hamlet.txt"));

        // Monkey writes drafts
        fs.addContentToFile("/jungle/banana_stories/drafts/draft1.txt", "Banana banana banana\n");
        fs.addContentToFile("/jungle/banana_stories/drafts/draft2.txt", "Oops typo 🍌\n");

        System.out.println("📝 Drafts created:");
        System.out.println("ls(/jungle/banana_stories/drafts): " + fs.ls("/jungle/banana_stories/drafts"));
        System.out.println();

        // Monkey tries to delete non-empty folder without -r
        try {
            System.out.println("🙈 Monkey tries to delete drafts WITHOUT recursive...");
            fs.rm("/jungle/banana_stories/drafts", false);
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

        System.out.println();

        // Monkey deletes drafts properly
        System.out.println("🔥 Monkey uses -r like a pro...");
        fs.rm("/jungle/banana_stories/drafts", true);

        System.out.println("ls(/jungle/banana_stories): " + fs.ls("/jungle/banana_stories"));
        System.out.println();

        // Monkey deletes file
        System.out.println("🗑️ Monkey deletes hamlet.txt");
        fs.rm("/jungle/banana_stories/hamlet.txt", false);

        System.out.println("ls(/jungle/banana_stories): " + fs.ls("/jungle/banana_stories"));
        System.out.println();

        // Edge case: delete root
        try {
            System.out.println("💀 Monkey tries to delete root...");
            fs.rm("/", true);
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

        System.out.println("\n🐒 Monkey retires. Literature saved.");
    }
}