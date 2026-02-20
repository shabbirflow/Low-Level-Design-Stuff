package org.example;

public class LockerLocation {
    private final String locationId;
    private final String locationName;
    private final LockerManager lockerManager;

    public LockerLocation(String locationId, String locationName, LockerManager lockerManager) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.lockerManager = lockerManager;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public LockerManager getLockerManager() {
        return lockerManager;
    }
}
