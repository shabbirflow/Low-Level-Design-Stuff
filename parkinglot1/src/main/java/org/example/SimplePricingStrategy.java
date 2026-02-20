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
