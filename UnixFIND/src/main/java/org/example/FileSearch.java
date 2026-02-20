package org.example;

import java.util.ArrayList;
import java.util.List;

public class FileSearch {

    public List<FileSystemNode> find(FileSystemNode root, Filter filter){
        List<FileSystemNode> result = new ArrayList<>();
        dfs(root, filter, result);
        return result;
    }

    private void dfs(FileSystemNode node, Filter filter, List<FileSystemNode> result){
        if(node.isDirectory()) {
            for (FileSystemNode child : node.getChildren()) {
                dfs(child, filter, result);
            }
            return;
        }
        if(!filter.matches(node)) return;
        result.add(node);
    }
}
