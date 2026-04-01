import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Directory extends Node{
    Map<String, Node> children;

    Directory(String name) {
        super(name);
        this.children = new HashMap<>();
    }

    Node getChild(String name){
//        if(!children.containsKey(name)) throw new RuntimeException("PATH NOT FOUND: " + name);
        return children.get(name);
    }

    Directory getOrCreateDirectory(String name) {
        Node node = children.get(name);

        if (node != null) {
            if (node instanceof File) {
                throw new RuntimeException(name + " exists as a file");
            }
            return (Directory) node;
        }

        Directory dir = new Directory(name);
        children.put(name, dir);
        return dir;
    }

    File getOrCreateFile(String name) {
        Node node = children.get(name);

        if (node != null) {
            if (node instanceof Directory) {
                throw new RuntimeException(name + " exists as a directory");
            }
            return (File) node;
        }

        File file = new File(name);
        children.put(name, file);
        return file;
    }

    void removeChild(String name, boolean recursive) {
        Node node = children.get(name);
        if (node == null) {
            throw new RuntimeException(name + " does not exist");
        }
        if(node instanceof Directory){
            Directory dir = (Directory) node;
            if(!recursive && !dir.children.isEmpty()){
                throw new RuntimeException(name + " is not empty");
            }
        }

        children.remove(name);
    }
}
