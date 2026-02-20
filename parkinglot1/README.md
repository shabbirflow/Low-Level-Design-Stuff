# 🅿️ Parking Lot — LLD

A Low Level Design implementation of a multi-floor parking lot system. Handles vehicle parking and unparking, allocates compatible spots by vehicle type, issues tickets, and calculates time-based charges via a pluggable pricing strategy.

---

## 🧩 Class Design

```
ParkingLot
├── List<Floor>
│       └── List<Spot>
│               └── SpotType  (COMPACT | LARGE | MOTORCYCLE | HANDICAPPED)
├── SpotAllocator             ← finds first available compatible spot
└── PricingStrategy           ← computes charge from vehicle + duration

Vehicle  (base class)
├── Car        → requires COMPACT
├── Truck      → requires LARGE
└── Motorcycle → requires MOTORCYCLE

Ticket
├── Vehicle
├── Spot
└── LocalDateTime entryTime
```

### Classes

| Class | Role |
|-------|------|
| `ParkingLot` | Entry point. Exposes `park(vehicle)` → `Ticket` and `unpark(ticket)` → prints charge. |
| `Floor` | A single floor level. Holds a list of `Spot`s. |
| `Spot` | One parking space. Has a `SpotType`, occupied flag, and currently assigned vehicle. |
| `SpotType` | Enum: `COMPACT`, `LARGE`, `MOTORCYCLE`, `HANDICAPPED` |
| `Vehicle` | Abstract base. Each subclass declares its required `SpotType`. |
| `SpotAllocator` | Iterates floors and spots to find the first available spot matching the vehicle's type. |
| `Ticket` | Issued at park time. Stores vehicle, spot, and `entryTime`. Acts as a receipt for unparking. |
| `PricingStrategy` | Interface: `double calculatePrice(Vehicle vehicle, long durationSeconds)` |
| `SimplePricingStrategy` | Flat rate per second, potentially varying by vehicle type. |

---

## ⚙️ Design Decisions

### Strategy Pattern — Pricing
`ParkingLot` holds a `PricingStrategy` interface reference. Different pricing models (dynamic surge, flat rate, membership discount) are swapped in at construction — no changes to `ParkingLot`:
```java
ParkingLot lot = new ParkingLot(floors, allocator, new SimplePricingStrategy());
// or:
ParkingLot lot = new ParkingLot(floors, allocator, new SurgePricingStrategy());
```

### Strategy Pattern — Spot Allocation
`SpotAllocator` is also injected, making the allocation algorithm swappable (e.g., nearest-to-entrance, floor-preference, handicapped-priority):
```java
// Current: first available matching spot
Spot spot = spotAllocator.allocate(vehicle, floors);
```

### Vehicle ↔ SpotType Matching
Each `Vehicle` subclass returns its required `SpotType`. The allocator finds the first `Spot` where:
- `spot.getType() == vehicle.getRequiredSpotType()`
- `!spot.isOccupied()`

This avoids any `instanceof` checks — type compatibility is determined by the vehicle itself.

### Ticket as Immutable Receipt
`Ticket` captures `vehicle`, `spot`, and `LocalDateTime.now()` at park time. These fields are never mutated. On unpark, duration is computed as:
```java
long duration = Duration.between(ticket.getEntryTime(), LocalDateTime.now()).toSeconds();
double charge = pricingStrategy.calculatePrice(ticket.getVehicle(), duration);
```

### Spot State Management
```java
spot.assignVehicle(vehicle)   // on park:   marks occupied
spot.removeVehicle()          // on unpark: marks free, clears vehicle reference
```

---

## 🔄 Request Flows

### Park
```
park(vehicle)
  1. SpotAllocator.allocate(vehicle, floors)
       → iterate floors → iterate spots
       → find first: type matches AND !occupied
  2. Ticket(vehicle, spot, LocalDateTime.now())
  3. spot.assignVehicle(vehicle)
  4. return Ticket
```

### Unpark
```
unpark(ticket)
  1. spot.removeVehicle()
  2. duration = Duration.between(ticket.entryTime, now).toSeconds()
  3. charge = pricingStrategy.calculatePrice(ticket.vehicle, duration)
  4. print duration + charge
```

---

## 📊 Spot Type Matching

| Vehicle | Required SpotType |
|---------|------------------|
| `Car` | `COMPACT` |
| `Truck` | `LARGE` |
| `Motorcycle` | `MOTORCYCLE` |

---

## 🧠 Design Patterns Used

| Pattern | Where |
|---------|-------|
| **Strategy** | `PricingStrategy` — swappable pricing algorithms |
| **Strategy** | `SpotAllocator` — swappable allocation algorithms |
| **Facade** | `ParkingLot` hides floors, spots, allocator, pricing behind 2 methods |
| **Template Method** | `Vehicle` base class with abstract `getRequiredSpotType()` |

---

## 📁 Source Files

```
src/main/java/org/example/
├── ParkingLot.java             ← Entry point: park + unpark
├── Floor.java                  ← Floor with list of spots
├── Spot.java                   ← Single parking space + state
├── SpotType.java               ← COMPACT / LARGE / MOTORCYCLE / HANDICAPPED
├── Vehicle.java                ← Abstract base: getRequiredSpotType()
├── SpotAllocator.java          ← First-fit allocation algorithm
├── Ticket.java                 ← Park receipt: vehicle + spot + entryTime
├── SimplePricingStrategy.java  ← Flat rate per second pricing
└── Main.java                   ← Demo
```

## ▶️ Running

```bash
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```
