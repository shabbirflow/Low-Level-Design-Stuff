package org.example;

public class Parcel {
    private final String id;
    private final LockerSize size;

    public Parcel(String id, LockerSize size) {
        this.id = id;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public LockerSize getSize() {
        return size;
    }
}
