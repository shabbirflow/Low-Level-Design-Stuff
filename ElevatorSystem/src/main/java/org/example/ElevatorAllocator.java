package org.example;

import java.util.List;
//External pickup request arrives.
//Controller queues it.
//Controller finds eligible elevators.
//Controller assigns exactly one elevator.
//Elevator accepts the pickup.
//Elevator moves step-by-step.
//Elevator reaches pickup floor, opens doors.
//Internal destination is added.
//Elevator continues until idle again.
//Controller reacts to elevator state changes

public interface ElevatorAllocator {
    Elevator selectElevator( List<Elevator> eligibleElevators,
                             PickupRequest request
    );
}
