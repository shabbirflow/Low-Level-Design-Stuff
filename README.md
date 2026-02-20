# 🏗️ Low Level Design — Java Implementation Collection

A curated collection of **8 production-grade Low Level Design (LLD)** implementations in Java. Each project demonstrates clean OOP principles, SOLID design, and real-world system design patterns — the kind asked in system design interviews at top tech companies.

---

## 📂 Projects Overview

| # | Project | Key Patterns |
|---|---------|-------------|
| 1 | [Amazon Locker System](#1-amazon-locker-system) | Strategy, State, Facade |
| 2 | [Coffee Shop](#2-coffee-shop) | Decorator, Builder, Validator |
| 3 | [Elevator System](#3-elevator-system) | State Machine, Command, Strategy |
| 4 | [LRU Cache](#4-lru-cache) | Doubly Linked List + HashMap |
| 5 | [Rate Limiter](#5-rate-limiter) | Strategy, Template Method |
| 6 | [SplitWise](#6-splitwise) | Strategy, SRP, Greedy Algorithm |
| 7 | [Unix FIND](#7-unix-find) | Composite, Chain of Responsibility |
| 8 | [Parking Lot](#8-parking-lot) | Strategy, Factory, State |

---

## 1. Amazon Locker System

**Folder:** `AmazonLockerSystem/`

### Problem
Design a system like Amazon's package delivery lockers — where parcels are stored in physical lockers at a location, customers get a pickup code, and retrieve their parcel using it.

### Key Classes

| Class | Responsibility |
|-------|---------------|
| `LockerSystem` | Top-level facade; manages multiple `LockerLocation`s |
| `LockerLocation` | Represents a physical locker station (e.g., a Whole Foods) |
| `LockerManager` | Handles locker allocation, pickup code generation, and parcel release |
| `Locker` | A single physical locker; tracks state (occupied/free/expired) |
| `Parcel` | The package being delivered, with a size |
| `LockerSize` | Enum: `SMALL`, `MEDIUM`, `LARGE` |

### Design Decisions

- **Size Upgrade Logic:** If no exact-size locker is available, the system automatically upgrades to a larger locker (SMALL → MEDIUM → LARGE). This is handled cleanly in `LockerManager.findAvailableLocker()`.
- **Pickup Code:** A UUID is generated per delivery and stored in `activePickups` (a `Map<String, Locker>`) for O(1) lookup at pickup time.
- **Expiry Cleanup:** `cleanupExpiredLockers()` scans active pickups and releases expired lockers, making them available again.
- **Facade Pattern:** `LockerSystem` provides a clean API (`deliverParcel`, `pickupParcel`) hiding all internal complexity.

### Flow

```
deliverParcel(locationId, parcel)
  → LockerLocation → LockerManager
  → findAvailableLocker(size)  [with upgrade fallback]
  → locker.assignParcel(parcel, code, ttl)
  → return pickupCode

pickupParcel(locationId, code)
  → LockerManager.pickupParcel(code)
  → locker.releaseParcel(code)
  → return Parcel
```

---

## 2. Coffee Shop

**Folder:** `CoffeeShop/`

### Problem
Design a coffee ordering system that allows customers to order beverages with various sizes, add customizable add-ons (extra shots, milk alternatives, toppings), enforce add-on rules, and compute a total order price.

### Key Classes

| Class | Responsibility |
|-------|---------------|
| `Beverage` | A drink with a name, size, base prices, and add-ons |
| `Size` | Enum: `SMALL`, `MEDIUM`, `LARGE` |
| `AddOn` | A single add-on type (name + price) |
| `AddOnItem` | An add-on with a quantity |
| `Order` | A collection of `OrderItem`s |
| `OrderItem` | A `Beverage` + quantity pair |
| `BeverageValidator` | Enforces business rules (e.g., max 3 extra shots per beverage) |

### Design Decisions

- **Decorator-inspired pricing:** Each `Beverage` holds a list of `AddOnItem`s. `calculatePrice()` sums the base size price with each add-on's cost × quantity.
- **Validation at add-on time:** `BeverageValidator` is injected into `Beverage` and validates every `addAddOn()` call, keeping business rules centralized and testable.
- **Size-based pricing:** `Beverage` accepts a `Map<Size, Double>` of base prices, making it trivially extensible to new sizes.

### Flow

```
Beverage latte = new Beverage("Latte", Size.LARGE, prices, validator)
latte.addAddOn(new AddOnItem(extraShot, 2))  ← throws if rule violated
Order order = new Order()
order.addItem(new OrderItem(latte, 1))
order.calculateSubtotal()  → iterates OrderItems → Beverage.calculatePrice()
```

---

## 3. Elevator System

**Folder:** `ElevatorSystem/`

### Problem
Design a multi-elevator control system that handles external pickup requests (user presses up/down on a floor) and internal requests (user selects destination floor inside the elevator).

### Key Classes

| Class | Responsibility |
|-------|---------------|
| `ElevatorController` | Receives external requests; assigns them to the best elevator |
| `Elevator` | A single elevator; manages its floor queue and movement |
| `ElevatorStatus` | Holds current floor, direction, and load of an elevator |
| `ElevatorAllocator` | Helper for selecting the optimal elevator |
| `PickupRequest` | An external floor + direction request |
| `Utils` | Shared utility methods |

### Design Decisions

- **Assignment Algorithm:** The controller filters elevators using `canAccept(request)` (checks direction compatibility and capacity), then picks the one with minimum distance. Idle elevators are preferred naturally since distance from idle = 0 in the right direction.
- **Pending Queue:** Requests that can't be immediately assigned (all elevators busy) are queued in a `LinkedList<PickupRequest>` and retried each `step()` tick.
- **Step-based simulation:** The `Controller.step()` method moves all elevators one step and re-tries unassigned requests — simulating real-time tick processing.
- **SRP:** `ElevatorController` handles dispatching, `Elevator` handles movement, `ElevatorStatus` is a pure data object.

### Flow

```
handleExternalRequest(floor, direction)
  → enqueue PickupRequest
  → tryAssignRequests()
    → for each request: selectElevator() → Elevator.acceptPickup()

step()  [called on each time tick]
  → each Elevator.step()  [moves 1 floor, opens doors]
  → tryAssignRequests()   [retry pending requests]
```

---

## 4. LRU Cache

**Folder:** `LRU_CACHe/`

### Problem
Implement a generic, fixed-capacity Least Recently Used (LRU) cache with O(1) `get` and O(1) `put` operations.

### Key Classes

| Class | Responsibility |
|-------|---------------|
| `LRUCache<K, V>` | The cache; backed by a HashMap + Doubly Linked List |
| `CacheEntry<K, V>` | A node in the doubly linked list; holds key, value, prev/next pointers |

### Design Decisions

- **Data Structure:** A `HashMap<K, CacheEntry>` gives O(1) key lookup. A **doubly linked list** maintains access order — most recently used at the `head`, least recently used at the `tail`.
- **Sentinel Nodes:** `head` and `tail` are dummy sentinel nodes, eliminating null checks in all pointer manipulation operations.
- **Eviction:** When capacity is full on `put`, the node at `tail.prev` (the LRU entry) is removed from both the list and the map.
- **Generic:** `LRUCache<K, V>` is fully generic — works for any key and value types.

### Operations

| Op | Steps | Complexity |
|----|-------|------------|
| `get(key)` | Lookup in map → `moveToFront()` → return value | O(1) |
| `put(key, val)` | Lookup → update or create → `addToFront()` → evict if needed | O(1) |

---

## 5. Rate Limiter

**Folder:** `Rate_Limiter/`

### Problem
Design a pluggable rate limiter that can enforce different rate-limiting policies (Token Bucket, Fixed Window, etc.) per user/API key.

### Key Classes

| Class | Responsibility |
|-------|---------------|
| `RateLimiter` | Entry point; delegates to a `RateLimitPolicy` |
| `RateLimitPolicy` | Interface: `allowRequest(key, currentTimeMillis)` |
| `TokenBucketPolicy` | Implements token bucket algorithm per key |
| `TokenBucket` | State object: current token count + last refill timestamp |
| `FixedWindowPolicy` | Implements fixed window counter algorithm |
| `WindowCounter` | State object for fixed window: count + window start time |

### Design Decisions

- **Strategy Pattern:** `RateLimiter` holds a `RateLimitPolicy` interface reference. Swapping from Token Bucket to Fixed Window is a one-line constructor change — no if/else chains.
- **Per-key state:** Both policies use a `Map<String, BucketOrCounter>` to maintain independent rate limits per API key or user ID.
- **Token Bucket:** Tokens refill continuously based on elapsed time (`tokensToAdd = elapsedMs × refillRatePerMs`). A request is allowed only if `tokens >= 1`, then 1 token is consumed.
- **Fixed Window:** Counts requests within a fixed time window. Resets the counter when a new window starts.

### Algorithms

```
Token Bucket:
  on request(key, now):
    refill(bucket, now)  → tokens += elapsed × rate, capped at capacity
    if tokens >= 1: tokens--; allow
    else: reject

Fixed Window:
  on request(key, now):
    if now > windowStart + windowSize: reset counter
    if count < limit: count++; allow
    else: reject
```

---

## 6. SplitWise

**Folder:** `SplitWise/`

### Problem
Design a bill-splitting application where users in a group can add expenses, split them by different methods, track who owes whom, and generate a minimal settlement plan.

### Key Classes

| Class | Responsibility |
|-------|---------------|
| `User` | A participant with a name and ID |
| `Group` | A set of users sharing expenses |
| `Expense` | A single expense with payer, participants, amount, and split strategy |
| `SplitStrategy` | Interface for computing per-user shares |
| `EqualSplitStrategy` | Divides amount equally among all participants |
| `ExactSplitStrategy` | Uses explicit per-user amounts |
| `PercentageSplitStrategy` | Splits by percentage |
| `BalanceSheet` | Tracks pairwise balances (`who owes whom how much`) |
| `SettlementService` | Computes the minimal set of transactions to settle all debts |
| `ExpenseValidator` | Validates that split amounts sum to total |

### Design Decisions

- **Strategy Pattern for Splits:** `SplitStrategy` is an interface with three implementations. Adding a new split type (e.g., share-based) requires only a new class — no modification to `Expense`.
- **BalanceSheet is the source of truth:** It stores raw pairwise balances. `SettlementService` does NOT mutate it — it computes a derived settlement plan, keeping concerns separate.
- **Minimal Settlements (Greedy Algorithm):** `SettlementService.settleUp()` flattens pairwise balances into net balances per user, then greedily matches the largest debtor with the largest creditor — minimizing total transactions.
- **SRP strictly followed:** `BalanceSheet` → state, `SettlementService` → algorithm, `ExpenseValidator` → validation.

### Settlement Algorithm

```
1. Compute net[user] = sum of what they owe - sum of what they're owed
2. Split into debtors (net > 0) and creditors (net < 0)
3. Greedy: match debtor ↔ creditor:
   settledAmount = min(owes, isOwed)
   → create Settlement(payer, amount, receiver)
   → remainder back to queue
4. Repeat until queues empty
```

---

## 7. Unix FIND

**Folder:** `UnixFIND/`

### Problem
Design a file search utility like the Unix `find` command — traverse a file system tree and return all files matching a given filter (by name, extension, size, or a combination).

### Key Classes

| Class | Responsibility |
|-------|---------------|
| `FileSystemNode` | Represents a file or directory; has name, extension, size, children |
| `FileSearch` | DFS traversal engine |
| `Filter` | Interface: `matches(FileSystemNode)` |
| `NameContainsFilter` | Matches files whose name contains a substring |
| `ExtensionsFilter` | Matches files by extension (e.g., `.java`) |
| `SizeFilter` | Matches files by size with operators `GT`, `LT`, `EQ` |
| `CompositeFilter` | Combines multiple filters with AND / OR logic |

### Design Decisions

- **Composite Pattern:** `CompositeFilter` holds a list of `Filter`s and can combine them with AND/OR — allowing arbitrarily complex filters without nested if/else logic.
- **Filter as Interface:** New filter types can be added (e.g., `ModifiedDateFilter`) with zero changes to `FileSearch` or existing filters — pure Open/Closed Principle.
- **DFS Traversal:** `FileSearch.find()` runs a recursive DFS. Directories are traversed but never returned as results. Only leaf files that match the filter are added.
- **Decoupled traversal from filtering:** The traversal logic knows nothing about filter specifics. Filters only know how to evaluate a single `FileSystemNode`.

### Flow

```
find(root, filter)
  → dfs(root, filter, results)
    → if directory: recurse into children
    → if file: filter.matches(node) → add to results
```

---

## 8. Parking Lot

**Folder:** `parkinglot1/`

### Problem
Design a multi-floor parking lot system that handles parking and unparking of different vehicle types, allocates appropriate spots, and computes parking charges based on duration.

### Key Classes

| Class | Responsibility |
|-------|---------------|
| `ParkingLot` | Top-level entry point: `park()` and `unpark()` |
| `Floor` | A level in the parking structure; contains a list of `Spot`s |
| `Spot` | An individual parking spot; knows its type and occupied state |
| `SpotType` | Enum: `COMPACT`, `LARGE`, `MOTORCYCLE`, `HANDICAPPED` |
| `Vehicle` | Base class for vehicles; each subtype declares its required `SpotType` |
| `SpotAllocator` | Finds and allocates the first available compatible spot |
| `Ticket` | Issued on park; stores vehicle, spot, and entry timestamp |
| `PricingStrategy` | Interface: `calculatePrice(vehicle, durationSeconds)` |
| `SimplePricingStrategy` | Charges a flat rate per second based on vehicle type |

### Design Decisions

- **Strategy Pattern for Pricing:** `ParkingLot` holds a `PricingStrategy` reference. Pricing rules (dynamic surge, flat-rate, membership discounts) can be swapped without changing `ParkingLot`.
- **Strategy Pattern for Allocation:** `SpotAllocator` can be swapped for smarter algorithms (nearest-to-entrance, floor-preference) without modifying `ParkingLot`.
- **Vehicle ↔ SpotType matching:** Each `Vehicle` subclass returns its required `SpotType`. The `SpotAllocator` iterates floors and spots to find the first available compatible spot.
- **Ticket as a receipt:** The `Ticket` captures vehicle, spot, and `LocalDateTime.now()` at entry. On unpark, duration is computed from `entryTime` to `now` using `Duration.between`.
- **Immutable after creation:** `Ticket` fields are set at construction and never mutated — safe to use as a reference.

### Flow

```
park(vehicle)
  → SpotAllocator.allocate(vehicle, floors)  → find first compatible free spot
  → Ticket(vehicle, spot, LocalDateTime.now())
  → spot.assignVehicle(vehicle)
  → return Ticket

unpark(ticket)
  → spot.removeVehicle()
  → duration = now - ticket.entryTime
  → charge = pricingStrategy.calculatePrice(vehicle, duration)
  → print charge
```

---

## 🛠️ Tech Stack

- **Language:** Java 17+
- **Build Tool:** Maven (`pom.xml` in each project)
- **IDE:** IntelliJ IDEA (`.idea/` config included)

## 🚀 Running a Project

Each project is an independent Maven module. To run any project:

```bash
cd <ProjectFolder>
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```

Or open the folder in IntelliJ IDEA and run `Main.java` directly.

---

## 🧠 Design Principles Applied

| Principle | Applied In |
|-----------|-----------|
| **Single Responsibility (SRP)** | SplitWise (BalanceSheet vs SettlementService), Elevator (Controller vs Elevator vs Status) |
| **Open/Closed (OCP)** | Rate Limiter (new policy = new class), Unix FIND (new filter = new class) |
| **Liskov Substitution (LSP)** | Vehicle subtypes in Parking Lot, SplitStrategy implementations |
| **Interface Segregation (ISP)** | `RateLimitPolicy`, `SplitStrategy`, `Filter`, `PricingStrategy` |
| **Dependency Inversion (DIP)** | RateLimiter depends on `RateLimitPolicy` interface, not concrete classes |
| **Strategy Pattern** | Rate Limiter, Parking Lot pricing & allocation, SplitWise split types |
| **Composite Pattern** | Unix FIND's `CompositeFilter` |
| **Facade Pattern** | Amazon Locker's `LockerSystem`, Parking Lot's `ParkingLot` |
| **State Pattern** | Elevator movement and direction tracking |
