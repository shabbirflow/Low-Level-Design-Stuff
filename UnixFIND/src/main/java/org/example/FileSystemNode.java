package org.example;

import java.util.List;

public class FileSystemNode {
    private final boolean isDirectory;
    private final long size;
    private final String name;
    private final List<FileSystemNode> children;

    public FileSystemNode(long size, String name) {
        this.isDirectory = false;
        this.size = size;
        this.name = name;
        this.children = null;
    }

    public FileSystemNode(String name){
        this.isDirectory = true;
        this.size = 0L;
        this.name = name;
        this.children = new java.util.ArrayList<>();
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public long getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public List<FileSystemNode> getChildren() {
        return children;
    }

    public void addChild(FileSystemNode f){
        if(!isDirectory) throw new IllegalStateException("NOT A DIRECTORY");
        children.add(f);
    }

    public String getExtension(){
        int idx = name.lastIndexOf('.');
        return idx == -1 ? "" : name.substring(idx);
    }
}
