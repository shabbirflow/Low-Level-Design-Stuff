package org.example;

import java.util.HashMap;
import java.util.Map;

public class LockerSystem {

    private final Map<String, LockerLocation> locations = new HashMap<>();

    public void addLocation(LockerLocation lockerLocation){
        locations.put(lockerLocation.getLocationId(), lockerLocation);
    }

    public String deliverParcel(String locationId, Parcel parcel){
        LockerLocation location = locations.get(locationId);
        if(location == null) throw new IllegalArgumentException("INVALID LOCATION!");
        return location.getLockerManager().deliverParcel(parcel);
    }

    public Parcel pickupParcel(String locationId, String pickupCode){
        LockerLocation location = locations.get(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Invalid location");
        }
        return location.getLockerManager().pickupParcel(pickupCode);
    }

}
