# 🏗️ Low Level Design (LLD) Collection

A comprehensive collection of production-grade **Low Level Design (LLD)** implementations in Java. This repository serves as a reference for clean code, SOLID principles, and common architectural patterns used in building robust, scalable systems.

---

## 📂 Project Index

This repository contains multiple versions and variations of common LLD problems, including simplified "standard" versions and more advanced "coaching" implementations.

### Core Projects

| Project | Key Patterns | Documentation |
|---------|-------------|---------------|
| **Amazon Locker System** | Strategy, State, Facade | [Standard](AmazonLockerSystem/README.md) \| [Advanced](AmazonLockerSystem_Educative/README.md) |
| **Chess Game** | Strategy, Template Method, Facade | [Detailed Design](Chess/README.md) |
| **Coffee Shop** | Decorator, Builder, Validator | [Detailed Design](CoffeeShop/README.md) |
| **Elevator System** | State Machine, Dispatcher, Singleton | [Standard](ElevatorSystem/README.md) \| [Advanced](ELEVATOR_SYSTEM_DUMPER/README.md) |
| **LRU Cache** | Doubly Linked List + HashMap | [Detailed Design](LRU_CACHe/README.md) |
| **Parking Lot** | Strategy, Factory, State | [Standard](parkinglot1/README.md) \| [Advanced](PARKING_LOT_Educative/README.md) |
| **Rate Limiter** | Strategy, Token Bucket, Fixed Window | [Detailed Design](Rate_Limiter/README.md) |
| **SplitWise** | Strategy, Balance Sheet, Greedy Settlement | [Detailed Design](SplitWise/README.md) |
| **Unix FIND** | Composite, Chain of Responsibility | [Detailed Design](UnixFIND/README.md) |

---

## 🧠 Design Principles Applied

Across all implementations, the following principles are strictly followed to ensure maintainability and extensibility:

| Principle | Description |
|-----------|-------------|
| **SOLID** | Ensuring single responsibility, open/closed designs, and interface segregation. |
| **Strategy Pattern** | Used for swappable algorithms (e.g., pricing, allocation, rate limiting). |
| **Facade Pattern** | Providing simple interfaces to complex subsystems (e.g., LockerSystem, ElevatorController). |
| **State Pattern** | Managing complex object lifecycles (e.g., Elevator status, Locker availability). |
| **Composite Pattern** | Building hierarchical structures (e.g., Unix Find filters). |

---

## 🚀 Getting Started

Each project is located in its own directory and can be explored independently.

### Running a Project
Most projects are standard Java or Maven-based. To run a project:

1. **Navigate to the directory:**
   ```bash
   cd <Project_Directory>
   ```

2. **Run via Maven (if applicable):**
   ```bash
   mvn compile exec:java -Dexec.mainClass="org.example.Main"
   ```

3. **Or run the `Driver` or `Main` class directly** using your IDE (IntelliJ IDEA, Eclipse, etc.).

---

## 🛠️ Tech Stack
- **Language:** Java 17+
- **Build Tool:** Maven (where applicable)
- **Design Philosophy:** Object-Oriented Design (OOD)
