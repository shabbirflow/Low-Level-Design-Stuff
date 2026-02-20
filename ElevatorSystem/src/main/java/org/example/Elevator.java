package org.example;

import java.util.TreeSet;

class Elevator {

    private int currentFloor;
    private Direction direction;
    private DoorState doorState;

    private TreeSet<Integer> upStops = new TreeSet<>();
    private TreeSet<Integer> downStops = new TreeSet<>();

    public Elevator(int startFloor) {
        this.currentFloor = startFloor;
        this.direction = Direction.IDLE;
        this.doorState = DoorState.CLOSED;
    }

    public boolean canAccept(PickupRequest request) {
        if (direction == Direction.IDLE) return true;

        if (direction == Direction.UP) {
            return request.getDirection() == Direction.UP
                    && request.getFloor() > currentFloor;
        }

        if (direction == Direction.DOWN) {
            return request.getDirection() == Direction.DOWN
                    && request.getFloor() < currentFloor;
        }

        return false;
    }

    public void acceptPickup(PickupRequest request) {
        addStop(request.getFloor());
    }

    public void acceptInternalRequest(int destinationFloor) {
        addStop(destinationFloor);
    }

    private void addStop(int floor) {
        if (floor > currentFloor) upStops.add(floor);
        else if (floor < currentFloor) downStops.add(floor);
    }

    public void step() {
        resolveDirectionIfIdle();
        if (direction == Direction.IDLE) return;

        moveOneFloor();
        handleStopIfPresent();
        resolveDirectionAfterMovement();
    }

    private void resolveDirectionIfIdle() {
        if (direction != Direction.IDLE) return;

        if (!upStops.isEmpty()) direction = Direction.UP;
        else if (!downStops.isEmpty()) direction = Direction.DOWN;
    }

    private void moveOneFloor() {
        if (direction == Direction.UP) currentFloor++;
        else if (direction == Direction.DOWN) currentFloor--;
    }

    private void handleStopIfPresent() {
        boolean stopping = false;

        if (direction == Direction.UP && upStops.remove(currentFloor)) {
            stopping = true;
        }

        if (direction == Direction.DOWN && downStops.remove(currentFloor)) {
            stopping = true;
        }

        if (stopping) {
            doorState = DoorState.OPEN;
            System.out.println("🩸 Doors open at floor " + currentFloor);
            doorState = DoorState.CLOSED;
        }
    }

    private void resolveDirectionAfterMovement() {
        if (direction == Direction.UP && upStops.isEmpty()) {
            direction = downStops.isEmpty() ? Direction.IDLE : Direction.DOWN;
        }

        if (direction == Direction.DOWN && downStops.isEmpty()) {
            direction = upStops.isEmpty() ? Direction.IDLE : Direction.UP;
        }
    }

    public ElevatorStatus getStatus() {
        return new ElevatorStatus(currentFloor, direction);
    }
}
