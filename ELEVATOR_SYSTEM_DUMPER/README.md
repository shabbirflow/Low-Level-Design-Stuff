# 🛗 Advanced Elevator System — LLD

A sophisticated Low-Level Design implementation of an elevator management system featuring multi-car coordination, maintenance modes, overload detection, and a priority-based dispatcher.

---

## 🧩 Architectural Overview

The system is designed around a central `Building` that houses multiple `ElevatorCar`s. Interaction occurs via `HallPanel`s on each floor and `ElevatorPanel`s inside each car.

### Key Components

| Class | Responsibility |
|-------|---------------|
| `ElevatorSystem` | Singleton facade. Orchestrates the building, car monitoring, and request dispatching. |
| `Building` | Container for `Floor`s and `ElevatorCar`s. Manages the physical structure. |
| `ElevatorCar` | Represents a physical lift. Handles movement, floor registration, maintenance state, and door operations. |
| `Floor` | Represents a single level in the building with its own panels and displays. |
| `HallPanel` / `HallButton` | Entry points for users on floors to call the elevator (UP/DOWN). |
| `ElevatorPanel` / `ElevatorButton` | Internal control panel for passengers to select destination floors. |
| `Display` | Shows current floor and direction information to users. |

---

## ⚙️ Core Logic & Features

### 1. Dispatching Algorithm (Nearest Idle Car)
The `ElevatorSystem` employs a **Nearest Idle Car** strategy for external requests:
1. Collects a `FloorRequest` from a hall panel.
2. Iterates through all available cars.
3. Filters for cars that are `IDLE`, not in maintenance, and not overloaded.
4. Selects the car with the minimum absolute distance to the requested floor.
5. If no car is available, the request is re-queued for the next dispatcher cycle.

### 2. Status Monitoring
The system provides real-time monitoring of all elevator cars through the `monitoring()` method, which updates the `Display` of each car to show its current position and state.

### 3. Maintenance Mode
Individual cars can be taken out of service using `enterMaintenance()`. When in maintenance:
- The car state becomes `IDLE`.
- The dispatcher ignores the car for new requests.
- All current requests for that car are cleared.

### 4. Overload Handling
Cars track their current load. If the load exceeds the defined capacity:
- The car becomes `overloaded`.
- It will not move until the load is reduced.
- The dispatcher will not assign new requests to it.

---

## 🔄 Sequence Flows

### External Call (Hall)
1. User presses UP/DOWN on a `HallButton`.
2. `HallPanel` delegates to `ElevatorSystem.callElevator()`.
3. Request is added to the system queue.
4. `dispatcher()` finds the nearest idle car and assigns the target floor.

### Internal Request (Car)
1. Passenger presses a floor button on the `ElevatorPanel`.
2. `ElevatorPanel` calls `ElevatorCar.registerRequest(floor)`.
3. Car adds the floor to its internal destination list.
4. Car moves to the next destination in its path.

---

## 🧠 Design Patterns Applied

- **Singleton Pattern**: `ElevatorSystem` ensures a single point of control for the entire building.
- **Facade Pattern**: `ElevatorSystem` simplifies complex interactions between cars, floors, and panels into a simple API.
- **State Pattern**: `ElevatorState` (IDLE, MOVING, STOPPED) drives the internal logic of the car's movement.
- **Observer Pattern**: (Logical) Panels and Displays reflect the state of the cars and the system effectively.

---

## 📁 Project Structure

```
ELEVATOR_SYSTEM_DUMPER/
├── Building.java           ← Physical layout management
├── ElevatorCar.java        ← Movement and car-level logic
├── ElevatorSystem.java     ← Global controller and dispatcher
├── Floor.java              ← Floor layout and panel containers
├── HallPanel.java          ← External controls
├── ElevatorPanel.java      ← Internal controls
├── Driver.java             ← Simulation scenarios
└── ... (Enums and Buttons)
```

## ▶️ Running the Simulation

You can run the pre-defined scenarios in the `Driver` class to see the system in action:

```bash
javac *.java
java Driver
```
