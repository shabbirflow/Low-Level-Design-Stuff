public class File extends Node{
    StringBuilder content;

    File(String name) {
        super(name);
        this.content = new StringBuilder();
    }

    void append(String data) {
        content.append(data);
    }

    String read() {
        return content.toString();
    }
}
