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
