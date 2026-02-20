package org.example;

public class PickupRequest {
    int floor;
    Direction direction;

    public PickupRequest(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    public int getFloor() {
        return floor;
    }

    public Direction getDirection() {
        return direction;
    }
}
