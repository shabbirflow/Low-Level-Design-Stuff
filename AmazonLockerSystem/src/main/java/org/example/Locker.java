package org.example;

public class Locker {
    private final String lockerId;
    private final LockerSize size;
    public String pickupCode;
    private Parcel parcel;
    private boolean occupied;
    private long expiryTime;

    public Locker(String lockerId, LockerSize size) {
        this.lockerId = lockerId;
        this.size = size;
        this.occupied = false;
    }

    public boolean isExpired() {
        return occupied && System.currentTimeMillis() > expiryTime;
    }

    public void forceRelease(){
        this.parcel = null;
        this.pickupCode = null;
        this.occupied = false;
        System.out.println("⏰ Locker " + lockerId + " expired parcel removed.");
    }

    public String getLockerId() {
        return lockerId;
    }

    public LockerSize getSize() {
        return size;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void assignParcel(Parcel parcel, String pickupCode, long expiryDurationMillis){
        if(occupied){
            throw new IllegalStateException("OCCUPIED ALREADY!");
        }
        System.out.println("📦 Locker " + lockerId + " storing parcel " + parcel.getId());
        this.parcel = parcel;
        this.occupied = true;
        this.expiryTime = System.currentTimeMillis() + expiryDurationMillis;
        this.pickupCode = pickupCode;
    }

    public Parcel releaseParcel(String code){
        if(!occupied){
            throw new IllegalStateException("LOCKER EMPTY!");
        }
        if(!code.equals(pickupCode)){
            throw new IllegalArgumentException("WRONG PICKUP CODE!");
        }
        System.out.println("📤 Locker " + lockerId + " releasing parcel " + parcel.getId());

        Parcel released = this.parcel;
        this.parcel = null; this.occupied = false; this.pickupCode = null;

        return released;
    }
}
