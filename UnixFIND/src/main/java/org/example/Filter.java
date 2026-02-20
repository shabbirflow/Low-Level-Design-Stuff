package org.example;

interface Filter {
    boolean matches(FileSystemNode file);
}

class NameContainsFilter implements Filter{
    private final String substring;

    public NameContainsFilter(String substring) {
        this.substring = substring;
    }

    @Override
    public boolean matches(FileSystemNode file){
        return file.getName().contains(substring);
    }
}

class ExtensionsFilter implements Filter{
    private final String extension;

    public ExtensionsFilter(String extension) {
        this.extension = extension;
    }

    @Override
    public boolean matches(FileSystemNode file) {
        return file.getExtension().equals(extension);
    }
}

class SizeFilter implements Filter{

    enum Operator {
        GT, LT, EQ
    }

    private final long size;
    private final Operator operator;

    public SizeFilter(long size, Operator operator) {
        this.size = size;
        this.operator = operator;
    }

    @Override
    public boolean matches(FileSystemNode file) {
        return switch (operator) {
            case GT -> file.getSize() > size;
            case LT -> file.getSize() < size;
            case EQ -> file.getSize() == size;
            default -> false;
        };
    }
}
