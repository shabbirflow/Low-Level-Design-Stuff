import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FileSystem {
    Directory root;

    public FileSystem() {
        this.root = new Directory("");
    }

    Directory getParentDirectory(String path, boolean createIfMissing){
        String[] parts = path.split("/");

        Directory dir = root;
        // Traverse till second last part
        for(int i=1; i< parts.length-1; i++){
            String part = parts[i];
            if(part.isEmpty()) continue;

            Node node = dir.getChild(part);
            if(node == null){
                if(createIfMissing){
                    dir = dir.getOrCreateDirectory(part);
                }else{
                    throw new IllegalArgumentException("Directory " + part + " does not exist");
                }
            }else{
                if(node instanceof File){
                    throw new RuntimeException(part + " is a file! Cannot traverse!");
                }else{
                    dir = (Directory) node;
                }
            }
        }

        return dir;
    }

    void addContentToFile(String path, String content){
        Directory parent = getParentDirectory(path, true);
        String[] parts = path.split("/");
        String fileName = parts[parts.length - 1];
        if (fileName.isEmpty()) {
            throw new RuntimeException("Invalid file name");
        }
        File file = parent.getOrCreateFile(fileName);
        file.append(content);
    }

    void mkdir(String path){
        Directory dir = getParentDirectory(path, true);
        String[] parts = path.split("/");
        String dirName = parts[parts.length - 1];
        if (dirName.isEmpty()) {
            throw new RuntimeException("Invalid directory name");
        }
        dir.getOrCreateDirectory(dirName);
    }

    String readContentFromFile(String path){
        Directory dir = getParentDirectory(path, false);
        String[] parts = path.split("/");
        String fileName = parts[parts.length - 1];
        if (fileName.isEmpty()) {
            throw new RuntimeException("Invalid directory name");
        }
        Node node = dir.getChild(fileName);
        if (node == null) {
            throw new RuntimeException("File " + fileName + " does not exist");
        }
        if(node instanceof Directory){
            throw new RuntimeException("Cannot read from a directory");
        }
        File file = (File) node;

        return file.read();
    }

    List<String> ls(String path) {
        List<String> result = new ArrayList<>();

        // Case 1: root
        if (path.equals("/")) {
            result.addAll(root.children.keySet());
            Collections.sort(result);
            return result;
        }

        String[] parts = path.split("/");
        String name = parts[parts.length - 1];

        Directory parent = getParentDirectory(path, false);
        Node node = parent.getChild(name);

        if (node == null) {
            throw new RuntimeException("Path does not exist: " + path);
        }

        // Case 2: File
        if (node instanceof File) {
            result.add(node.name);
            return result;
        }

        // Case 3: Directory
        Directory dir = (Directory) node;
        result.addAll(dir.children.keySet());

        Collections.sort(result);
        return result;
    }

    void rm(String path, boolean recursive) {
        if (path.equals("/")) {
            throw new RuntimeException("Cannot delete root");
        }

        Directory parent = getParentDirectory(path, false);

        String[] parts = path.split("/");
        String name = parts[parts.length - 1];

        Node node = parent.getChild(name);
        if (node == null) {
            throw new RuntimeException("Path does not exist: " + path);
        }

        parent.removeChild(name, recursive);
    }
}
