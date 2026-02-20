package org.example;

public class ElevatorStatus {
    private int currentFloor;
    private Direction direction;

    public ElevatorStatus(int currentFloor, Direction direction) {
        this.currentFloor = currentFloor;
        this.direction = direction;
//        this.acceptingRequests = acceptingRequests;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

}
