package org.example;

import java.util.List;

interface SpotAllocator {
    Spot allocate(Vehicle v, List<Floor> floors);
}

class FirstAvailableSpotAllocator implements SpotAllocator{
    public Spot allocate(Vehicle vehicle, List<Floor> floors) {
        for (Floor floor : floors) {
            for (Spot spot : floor.getParkingSpots()) {
                if (spot.canFit(vehicle)) {
                    return spot;
                }
            }
        }
        return null;
    }
}

