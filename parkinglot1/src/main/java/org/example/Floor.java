package org.example;

import java.util.List;

public class Floor {
    List<Spot> parkingSpots;

    Floor(List<Spot> spots){
        this.parkingSpots = spots;
    }

    List<Spot> getParkingSpots(){
        return parkingSpots;
    }
}
