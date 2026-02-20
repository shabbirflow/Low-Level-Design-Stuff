# 🛗 Elevator System — LLD

A Low Level Design implementation of a multi-elevator dispatch system. Handles external pickup requests (floor buttons), assigns them to the optimal elevator, and simulates step-based movement.

---

## 🧩 Class Design

```
ElevatorController
├── List<Elevator>
│       ├── ElevatorStatus   (currentFloor, direction, load)
│       └── TreeSet<Integer> (destination floors)
└── Queue<PickupRequest>     (pending unassigned requests)

PickupRequest
└── floor, direction
```

### Classes

| Class | Role |
|-------|------|
| `ElevatorController` | Central dispatcher. Receives external requests, assigns them to the best elevator, and drives the simulation via `step()`. |
| `Elevator` | A single elevator car. Maintains its status, a sorted set of destination floors, and moves one floor per `step()`. |
| `ElevatorStatus` | Data object: `currentFloor`, `direction` (`UP`/`DOWN`/`IDLE`), current load. |
| `ElevatorAllocator` | Helper used by the controller to select the optimal elevator for a request. |
| `PickupRequest` | An external hall-button press: target floor + requested direction. |
| `Utils` | Shared utility methods. |

---

## ⚙️ Design Decisions

### Elevator Selection Algorithm
The controller filters all elevators using `canAccept(request)`, which checks:
- Elevator is moving in the same direction as the request, **and** the request floor is ahead of the elevator's current floor
- **OR** the elevator is `IDLE`

From the eligible set, it picks the one with **minimum distance** to the request floor:
```java
int distance = Math.abs(status.getCurrentFloor() - request.getFloor());
```

### Pending Request Queue
If no elevator can accept a request at dispatch time (all busy in the wrong direction), the request is queued in a `LinkedList<PickupRequest>`. The queue is retried on every `step()` tick — no request is dropped.

### Step-Based Simulation
```java
controller.step();
  → elevator.step()    // each elevator moves 1 floor, opens doors at destinations
  → tryAssignRequests() // retry any pending unassigned requests
```
This cleanly decouples physics (movement) from dispatch logic.

### SRP Strictly Followed
- `ElevatorController` — dispatching only
- `Elevator` — movement and door logic only
- `ElevatorStatus` — pure data, no logic
- `PickupRequest` — pure data

---

## 🔄 Request Flows

### External Request (Hall Button Press)
```
handleExternalRequest(floor, direction)
  1. Wrap into PickupRequest
  2. Enqueue in pickupRequests
  3. tryAssignRequests()
       → for each pending request:
           selectElevator(request)
             → filter: canAccept(request)
             → pick min distance
       → if found: elevator.acceptPickup(request); remove from queue
```

### Step Tick
```
step()
  → for each Elevator: elevator.step()
       → move 1 floor toward next destination
       → if arrived: open doors, remove from destination set, update direction
  → tryAssignRequests()   ← retry previously unassigned requests
```

---

## 🧠 Design Patterns Used

| Pattern | Where |
|---------|-------|
| **Command** | `PickupRequest` encapsulates a request as an object |
| **State** | `Elevator` direction (`UP`/`DOWN`/`IDLE`) drives movement decisions |
| **Strategy** | Elevator selection logic isolated in `ElevatorAllocator` |
| **Observer (informal)** | `step()` drives all elevators uniformly each tick |

---

## 📁 Source Files

```
src/main/java/org/example/
├── ElevatorController.java  ← Central dispatcher
├── Elevator.java            ← Single elevator car + movement
├── ElevatorStatus.java      ← Data: floor, direction, load
├── ElevatorAllocator.java   ← Optimal elevator selection
├── PickupRequest.java       ← External hall-button request
├── Utils.java               ← Shared utilities
└── Main.java                ← Demo / simulation driver
```

## ▶️ Running

```bash
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```
