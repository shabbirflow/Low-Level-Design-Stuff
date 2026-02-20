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
