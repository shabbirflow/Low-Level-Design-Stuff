package org.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ParkingLot {
    private List<Floor> floors;
    private SpotAllocator spotAllocator;
    private PricingStrategy pricingStrategy;

    ParkingLot(List<Floor> floors, SpotAllocator allocator, PricingStrategy pricingStrategy){
        this.floors = floors;
        this.spotAllocator = allocator;
        this.pricingStrategy = pricingStrategy;
    }

    Ticket park(Vehicle v){
        System.out.println("Attempting to park vehicle: " + v.getClass().getSimpleName());
//        System.out.println("ALLOCATING FOR VEHICLE: " + v.getRequiredSpotType());
        Spot s = spotAllocator.allocate(v, floors);
        if(s == null){
            throw new IllegalStateException("NO SPOT FOUND!");
        }
        Ticket ticket = new Ticket(v, s, LocalDateTime.now());
        System.out.println("TICKET CREATED! ");
        s.assignVehicle(v);
        System.out.println("Vehicle parked at spot type: " + s.getType());
        return ticket;
    }
    void unpark(Ticket t){
        Spot spot = t.getSpot();
        spot.removeVehicle();
        long duration =
                Duration.between(t.getEntryTime(), LocalDateTime.now()).toSeconds();

        double amount =
                pricingStrategy.calculatePrice(t.getVehicle(), duration);

        System.out.println("Parking duration (minutes): " + duration);
        System.out.println("Total parking charge: ₹" + amount);
    }
}
