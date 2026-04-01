# Library Management System - Low Level Design (LLD) Analysis

This document provides a comprehensive analysis of the Low Level Design for the Library Management System. Based on the Java source code, the system adopts an object-oriented approach to represent entities, interactions, and rules that govern a real-world library.

---

## 1. System Overview

The system is designed to allow library members to search for books, checkout books, return books, and reserve them. It also handles administrative capabilities for librarians to manage books and users, and includes fine calculation for overdue items.

The design relies heavily on strong object-oriented principles such as Inheritance, Polymorphism, and Abstraction to represent the real-world actors and items cleanly.

---

## 2. Core Entities and Data Models

### 2.1 Enums (State Management)
Enums are used effectively to limit states to a predefined set of constants, reducing invalid states in the system:
- **`AccountStatus`**: `ACTIVE`, `CLOSED`, `CANCELED`, `BLACKLISTED`, `NONE`
- **`BookStatus`**: `AVAILABLE`, `RESERVED`, `LOANED`, `LOST`
- **`ReservationStatus`**: `WAITING`, `PENDING`, `CANCELED`, `NONE`
- **`BookFormat`**: `HARDCOVER`, `PAPERBACK`, `AUDIOBOOK`, `EBOOK`, `NEWSPAPER`, `MAGAZINE`, `JOURNAL`

### 2.2 Base Data Types
- **`Address`**: Houses location-based details (`street`, `city`, `state`, `zipcode`, `country`).
- **`Person`**: A foundational class containing demographic data like `name`, `email`, `phone`, and an `Address` object.

### 2.3 User Hierarchy (Actors)
The actor model is built around a single abstract class branching into specific roles:
- **`User` (Abstract)**: Inherits user-specific behavior, linking a `Person` with authentication details (`id`, `password`), an `AccountStatus`, and a `LibraryCard`.
    - **`Member`**: Represents a patron. Handles logic such as tracking `totalBooksCheckedOut`, `booksBorrowed`, and `finesDue`. It contains precise business logic for `checkoutBookItem()`, `returnBookItem()`, `renewBookItem()`, and `reserveBookItem()`.
    - **`Librarian`**: Represents an admin. Exposes privileges like `addBookItem()` to the catalog and `blockMember()`/`unBlockMember()` functionality.
- **`Author`**: Extends `Person` rather than `User`, as authors are data entities associated with books rather than active actors who log into the system. Includes a `description`.

### 2.4 Representation of Books
The design separates the generic concept of a book from a physical copy. This is a crucial design decision that resembles the **Flyweight Pattern**:
- **`Book`**: Represents the universal details of a publication (`isbn`, `title`, `subject`, `publisher`, `format`, `authors`).
- **`BookItem`**: Represents a physical copy of a book in the library. It holds a reference to a `Book` object, along with physical state details like `id`, `price`, `status`, `dueDate`, `borrowed` date, and the `Rack` where it is physically placed.

### 2.5 Infrastructure
- **`Rack`**: Represents the physical location (`number`, `locationIdentifier`) where a `BookItem` is placed.
- **`LibraryCard`**: Associates an active ID and issue date to a `User`.

---

## 3. Core System Components

### 3.1 Catalog & Search (Interface Segregation)
- **`Search` (Interface)**: Defines the contract for looking up books: `searchByTitle`, `searchByAuthor`, `searchBySubject`, `searchByPublicationDate`.
- **`Catalog`**: Implements the `Search` interface. It stores the collection of all `BookItem`s using multiple `HashMap`s for O(1) average time complexity lookups based on different attributes (Title, Author, Subject, Publication Date).

### 3.2 System Controller
- **`Library`**: Follows the **Singleton Pattern** since there should typically only be one central library manager instance running the application. It acts as the central hub containing a `name`, `Address`, and a `Catalog`.

### 3.3 Transaction Tracking
- **`BookLending` & `BookReservation`**: These classes abstract the associations between a `BookItem` and a `Member`. They record timestamps (`creationDate`, `dueDate`, `returnDate`) and rely on statically mapped `HashMap`s to track active transactions across the system. 
- **`Fine`**: A utility class with a static method `collectFine()` to calculate fees for late returns based on days overdue.

### 3.4 Notifications
This module follows the **Strategy/Factory** style pattern using Inheritance to abstract notification sending:
- **`Notification` (Abstract)**: Base structure containing generic `notificationId`, `content`, and creation date.
- **`EmailNotification` & `PostalNotification`**: Concrete implementations that define how `sendNotification()` should happen based on the required channel.

---

## 4. Design Patterns Identified

1.  **Singleton Pattern**: Used in the `Library` class to ensure only a single instance of the library system exists during the application's runtime.
    ```java
    public static Library getInstance(String name, Address address) {
        if (instance == null) instance = new Library(name, address);
        return instance;
    }
    ```
2.  **Inheritance / Generalization**: Extensively used to model the `User` -> (`Member`, `Librarian`) relation, the `Person` -> `Author` relation, and the `Notification` -> (`EmailNotification`, `PostalNotification`) relationship.
3.  **Interface Segregation**: The `Search` interface cleanly abstracts out the searching capabilities from the storage specifics of the `Catalog`.
4.  **Composition / Aggregation**: 
    - A `User` *has a* `Person`, an `Address`, and a `LibraryCard`.
    - A `BookItem` *has a* `Book` and a `Rack`.
    - A `Library` *has a* `Catalog`.
5.  **Flyweight Pattern (Concept)**: Separating `Book` (metadata shared among potentially duplicate copies) from `BookItem` (instance-specific data like barcodes, due dates, rack placement) prevents memory bloat and duplicates.

---

## 5. Potential Improvements to the LLD

While the design is solid, here are a few suggestions to make it production-ready or to impress in an interview setting:

1.  **Concurrency (Thread Safety)**: The `Catalog` class uses standard `HashMap` structures. If multiple librarians or members run transactions concurrently, this will cause `ConcurrentModificationException` or data loss. Transitioning to `ConcurrentHashMap` or using synchronization blocks is necessary for a real-world multi-threaded application.
2.  **Decoupling Business Logic**: Currently, `Member` directly modifies states inside `BookItem` within the `returnBookItem` and `checkoutBookItem` methods. Ideally, an external service class (e.g., `CheckoutService`) should coordinate transactions between Members and BookItems to avoid tight coupling.
3.  **Dependency Injection**: The `Library` directly instantiates its own `Catalog`. Injecting the Catalog would make the `Library` class easier to unit test.
4.  **Pagination for Search Results**: Methods returning `List<BookItem>` in `Search` or `Catalog` might return thousands of objects. Overloads supporting pagination (e.g., `searchByTitle(String title, int limit, int offset)`) would improve performance.
5.  **Data Persistence layer**: Currently, lists and static maps store entities in memory. In a real system, classes would map to Database entities using an ORM like Hibernate or purely relying on repository patterns communicating with SQL/NoSQL databases.

---

## 6. Request Flows (How the system works together)

### Example: Member Checkout Flow
1. Member calls `library.catalog.searchByTitle("Title")` to get a `BookItem`.
2. Member triggers `member.checkoutBookItem(bookItem)`.
3. Validation happens: checks if member has reached their max book limit (`totalBooksCheckedOut >= 10`).
4. Calls `bookItem.checkout(memberId)` which verifies `BookItem` availability (`status == BookStatus.AVAILABLE`) and sets the `dueDate` (Current Date + 15 days).
5. If checkout is successful, `Member` adds `BookItem` to `booksBorrowed` list, changes the book's status to `BookStatus.LOANED`, and increments counters.
