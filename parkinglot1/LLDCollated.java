// Floor.java

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



// Main.java

package org.example;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        List<Spot> spots = List.of(
                new Spot(SpotType.BIKE),
                new Spot(SpotType.CAR),
                new Spot(SpotType.TRUCK)
        );

        Floor floor = new Floor(spots);

        ParkingLot lot = new ParkingLot(
                List.of(floor),
                new FirstAvailableSpotAllocator(),
                new SimplePricingStrategy()
        );

        Vehicle car = new Bike();
        Ticket ticket = lot.park(car);

        Thread.sleep(2000); // simulate time

        lot.unpark(ticket);
    }
}



// ParkingLot.java

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



// SimplePricingStrategy.java

package org.example;

import java.time.LocalDateTime;

interface PricingStrategy{
    double calculatePrice(Vehicle v, long durationInMinutes);
}

public class SimplePricingStrategy implements PricingStrategy {
    public double calculatePrice(Vehicle vehicle, long durationInMinutes){
        double ratePerHour;
        if (vehicle instanceof Bike) {
            ratePerHour = 10;
        } else if (vehicle instanceof Car) {
            ratePerHour = 20;
        } else {
            ratePerHour = 30; // Truck
        }

        double hours = Math.ceil(durationInMinutes / 60.0);
        return hours * ratePerHour;
    }
}



// Spot.java

package org.example;

public class Spot {
    SpotType type;
    Vehicle vehicle; // null if free

    Spot(SpotType spotType){
        this.type = spotType;
    }

    boolean canFit(Vehicle vehicle){
        return isFree() && vehicle.getRequiredSpotType().ordinal() <= type.ordinal();
    }

    boolean isFree(){
        return vehicle == null;
    }

    void assignVehicle(Vehicle v){
        if(!canFit(v)){
            throw new IllegalStateException("SPOT CANNOT FIT VEHICLE");
        }
        System.out.println("SPOT ASSIGNED TO VEHCLE!");
        this.vehicle = v;
    }
    void removeVehicle(){
        System.out.println("VEHICLE REMOVED FROM SPOT!");
        this.vehicle = null;
    }
    SpotType getType(){
        return type;
    }
}



// SpotAllocator.java

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




// SpotType.java

package org.example;

public enum SpotType {
    BIKE, CAR, TRUCK
}



// Ticket.java

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



// Vehicle.java

package org.example;

public interface Vehicle {

    public abstract SpotType getRequiredSpotType();
}

class Bike implements Vehicle{
    public SpotType getRequiredSpotType(){
        return SpotType.BIKE;
    }
}
class Car implements Vehicle {
    public SpotType getRequiredSpotType() {
        return SpotType.CAR;
    }
}

class Truck implements Vehicle {
    public SpotType getRequiredSpotType() {
        return SpotType.TRUCK;
    }
}



