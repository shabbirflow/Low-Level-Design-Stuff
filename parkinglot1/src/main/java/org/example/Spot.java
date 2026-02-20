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
