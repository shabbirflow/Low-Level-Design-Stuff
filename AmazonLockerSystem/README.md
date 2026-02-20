# 📦 Amazon Locker System — LLD

A Low Level Design implementation of Amazon's physical package delivery locker system, where packages are stored at a locker station and customers retrieve them with a pickup code.

---

## 🧩 Class Design

```
LockerSystem
└── Map<locationId, LockerLocation>
        └── LockerManager
                ├── Map<LockerSize, Queue<Locker>>   (availableLockers)
                ├── Map<pickupCode, Locker>           (activePickups)
                └── Locker[]
                        └── Parcel

Parcel
└── LockerSize  (SMALL | MEDIUM | LARGE)
```

### Classes

| Class | Role |
|-------|------|
| `LockerSystem` | Top-level facade. Entry point for `deliverParcel` and `pickupParcel`. Routes requests to the correct `LockerLocation`. |
| `LockerLocation` | Represents a physical locker station (e.g., a Whole Foods or apartment lobby). |
| `LockerManager` | Core logic: allocates lockers, generates pickup codes, handles releases and expiry cleanup. |
| `Locker` | A single physical locker unit. Tracks its size, assigned parcel, pickup code, TTL, and occupied state. |
| `Parcel` | The package being delivered. Has a `LockerSize` representing the smallest locker it fits into. |
| `LockerSize` | Enum: `SMALL`, `MEDIUM`, `LARGE` |

---

## ⚙️ Design Decisions

### Facade Pattern
`LockerSystem` exposes a clean 2-method API to the outside world:
```java
lockerSystem.deliverParcel(locationId, parcel);   // returns pickupCode
lockerSystem.pickupParcel(locationId, pickupCode); // returns Parcel
```
All internal complexity — locker sizing, code generation, state tracking — is hidden behind this facade.

### Size Upgrade Logic (Locker Fit Algorithm)
If no exact-size locker is available, the system automatically upgrades to the next larger size:
```
SMALL parcel  → try SMALL → try MEDIUM → try LARGE
MEDIUM parcel → try MEDIUM → try LARGE
LARGE parcel  → try LARGE only
```
Each size bucket is a `Queue<Locker>` so allocation is O(1) poll.

### Pickup Code Generation
A `UUID.randomUUID()` string is used as the pickup code — globally unique, unguessable. It's stored in `activePickups` (`Map<String, Locker>`) for O(1) retrieval at pickup time.

### Expiry & Cleanup
`cleanupExpiredLockers()` scans all active pickups, checks `locker.isExpired()`, and releases overdue lockers back to the available queue. Designed to be called by a background scheduler.

---

## 🔄 Request Flows

### Delivery Flow
```
deliverParcel(locationId, parcel)
  1. Lookup LockerLocation by locationId
  2. LockerManager.deliverParcel(parcel)
     a. findAvailableLocker(parcel.getSize())   ← size-upgrade logic
     b. code = UUID.randomUUID()
     c. locker.assignParcel(parcel, code, ttl)
     d. activePickups.put(code, locker)
  3. Return pickupCode to caller
```

### Pickup Flow
```
pickupParcel(locationId, pickupCode)
  1. Lookup LockerLocation by locationId
  2. LockerManager.pickupParcel(pickupCode)
     a. locker = activePickups.get(code)        ← O(1)
     b. activePickups.remove(code)
     c. parcel = locker.releaseParcel(code)
     d. availableLockers[size].offer(locker)    ← locker back to pool
  3. Return Parcel to customer
```

---

## 🧠 Design Patterns Used

| Pattern | Where |
|---------|-------|
| **Facade** | `LockerSystem` hides all internal complexity |
| **State** | `Locker` tracks `occupied`, `expired` states |
| **Object Pool** | Lockers returned to `Queue` after pickup, ready for reuse |

---

## 📁 Source Files

```
src/main/java/org/example/
├── LockerSystem.java       ← Facade, entry point
├── LockerLocation.java     ← Physical station
├── LockerManager.java      ← Core business logic
├── Locker.java             ← Single locker unit
├── Parcel.java             ← Package being delivered
├── LockerSize.java         ← SMALL / MEDIUM / LARGE enum
└── Main.java               ← Demo / driver
```

## ▶️ Running

```bash
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```
