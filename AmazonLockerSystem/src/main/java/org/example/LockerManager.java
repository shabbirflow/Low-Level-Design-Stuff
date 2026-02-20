package org.example;

import java.util.*;

public class LockerManager {
    private final Map<LockerSize, Queue<Locker>> availableLockers;
    private final Map<String, Locker> activePickups;

    public LockerManager(List<Locker> lockers){
        availableLockers = new HashMap<>();
        activePickups = new HashMap<>();

        for(LockerSize size : LockerSize.values()){
            availableLockers.put(size, new LinkedList<>());
        }

        for(Locker locker : lockers){
            availableLockers.get(locker.getSize()).offer(locker);
        }
    }

    public String deliverParcel(Parcel parcel){
        Locker locker = findAvailableLocker(parcel.getSize());

        if(locker == null){
            throw new IllegalStateException("NO LOCKER FOR SIZE: " + parcel.getSize());
        }

        String code = generatePickupCode();
        locker.assignParcel(parcel, code, 2000);
        activePickups.put(code, locker);

        return code;
    }

    private String generatePickupCode() {
        return UUID.randomUUID().toString();
    }

    public Parcel pickupParcel(String pickupCode){
        Locker locker = activePickups.get(pickupCode);
        if(locker == null){
            throw new IllegalStateException("LOCKER NOT FOUND! INVALID CODE");
        }

        activePickups.remove(pickupCode);
        Parcel p =locker.releaseParcel(pickupCode);
        availableLockers.get(locker.getSize()).offer(locker);

        return p;
    }
    private Locker findAvailableLocker(LockerSize requiredSize) {

        // Try exact size first
        Locker locker = pollLocker(requiredSize);
        if (locker != null) return locker;

        // Upgrade logic: allow larger locker
        if (requiredSize == LockerSize.SMALL) {
            locker = pollLocker(LockerSize.MEDIUM);
            if (locker != null) return locker;

            return pollLocker(LockerSize.LARGE);
        }

        if (requiredSize == LockerSize.MEDIUM) {
            return pollLocker(LockerSize.LARGE);
        }

        return null; // LARGE cannot upgrade further
    }

    private Locker pollLocker(LockerSize size) {
        return availableLockers.get(size).poll();
    }

    public void cleanupExpiredLockers() {

        for (Locker locker : activePickups.values()) {
            if (locker.isExpired()) {
                locker.forceRelease();
                availableLockers.get(locker.getSize()).offer(locker);
            }
        }

        activePickups.entrySet()
                .removeIf(entry -> !entry.getValue().isOccupied());
    }

}
