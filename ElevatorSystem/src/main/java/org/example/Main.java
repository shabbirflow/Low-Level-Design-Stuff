package org.example;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Elevator elevator1 = new Elevator(1);
        Elevator elevator2 = new Elevator(10);

        ElevatorController controller =
                new ElevatorController(List.of(elevator1, elevator2));

        System.out.println("\n🏚️  Haunted Building Simulation Starts\n");

        // ---- External pickup requests ----
        System.out.println("📞 External Request: Floor 3 wants to go UP");
        controller.handleExternalRequest(
                new PickupRequest(3, Direction.UP));

        System.out.println("📞 External Request: Floor 7 wants to go DOWN");
        controller.handleExternalRequest(
                new PickupRequest(7, Direction.DOWN));

        // ---- Time simulation ----
        for (int t = 1; t <= 5; t++) {
            System.out.println("\n⏱️  TIME STEP " + t);
            controller.step();
            printStatus(elevator1, "E1");
            printStatus(elevator2, "E2");
        }

        // ---- Internal request (opposite direction) ----
        System.out.println("\n😱 Passenger inside E1 presses floor 1 (DOWN)");
        elevator1.acceptInternalRequest(1);

        for (int t = 6; t <= 10; t++) {
            System.out.println("\n⏱️  TIME STEP " + t);
            controller.step();
            printStatus(elevator1, "E1");
            printStatus(elevator2, "E2");
        }

        System.out.println("\n🪦 Simulation ends. All elevators are idle.");
    }

    private static void printStatus(Elevator e, String name) {
        ElevatorStatus s = e.getStatus();
        System.out.println(
                name + " -> Floor: " + s.getCurrentFloor()
                        + ", Direction: " + s.getDirection()
        );
    }
}
