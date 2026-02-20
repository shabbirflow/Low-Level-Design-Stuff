package org.example;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//Filter elevators using canAccept(request)
//From the eligible set:
//Prefer idle elevators.
//Otherwise, minimize distance.
//Assign exactly one elevator.

public class ElevatorController {
    private List<Elevator> elevatorList;
    private Queue<PickupRequest> pickupRequests;

    public ElevatorController(List<Elevator> elevatorList) {
        this.pickupRequests = new LinkedList<>();
        this.elevatorList = elevatorList;
    }

    public void handleExternalRequest(PickupRequest pickupRequest){
        // try to assign
        pickupRequests.offer(pickupRequest);
        tryAssignRequests();

    }

    public void tryAssignRequests(){

        Iterator<PickupRequest> iterator = pickupRequests.iterator();

        while(iterator.hasNext()){
            PickupRequest request = iterator.next();
            Elevator best = selectElevator(request);
            if(best != null){
                best.acceptPickup(request);
                iterator.remove();
            }
        }
    }

    private Elevator selectElevator(PickupRequest request) {
        Elevator chosen = null;
        int minDistance = Integer.MAX_VALUE;

        for(Elevator elevator : elevatorList){
            if(!elevator.canAccept(request)) continue;

            ElevatorStatus status = elevator.getStatus();
            int distance = Math.abs(status.getCurrentFloor() - request.getFloor());

            if(distance < minDistance){
                minDistance = distance;
                chosen = elevator;
            }
        }

        return chosen;
    }

    public void step() {
        for (Elevator elevator : elevatorList) {
            elevator.step();
        }
        tryAssignRequests();
    }
}
