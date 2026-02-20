package org.example;

import java.time.LocalDateTime;

public class Ticket {
    Vehicle vehicle;
    Spot spot;
    LocalDateTime entryTime;

    Ticket(Vehicle v, Spot s, LocalDateTime time){
        this.vehicle = v;
        this.spot = s;
        this.entryTime = time;
    }

    Spot getSpot() {
        return spot;
    }

    Vehicle getVehicle() {
        return vehicle;
    }

    LocalDateTime getEntryTime() {
        return entryTime;
    }
}
