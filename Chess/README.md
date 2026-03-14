# Chess Game Low-Level Design (LLD)

This document provides a comprehensive and detailed explanation of the Low-Level Design (LLD) of the Chess game implemented in Java. It covers the core requirements, design patterns, class structures, entity relations, and the flow of the game.

## 1. System Requirements & Assumptions
- **Board:** An 8x8 standard chessboard.
- **Pieces:** 16 pieces for each player (1 King, 1 Queen, 2 Rooks, 2 Bishops, 2 Knights, 8 Pawns).
- **Players:** Two players (White and Black).
- **Moves:** Pieces move according to standard chess rules.
- **Rules Supported (Core):** Basic moves, captures, collision checking (path blocking), turns.
- **Rules Deferred (Extensions):** Check, Checkmate, Stalemate, Castling, En Passant, Pawn Promotion (partially implemented).
- **History:** The game must maintain a complete history of all moves.
- **State:** The game can be Active, Draw, White Win, Black Win, etc.

---

## 2. Core Entities & Class Hierarchy

The design follows classical Object-Oriented Programming (OOP) principles, ensuring high cohesion and low coupling. 

### 2.1 The Board and Squares
- **`Box` (Square):** Represents a single cell on the board. It holds its `x` and `y` coordinates and maintains a reference to the `Piece` currently sitting on it. If empty, `piece` is `null`.
- **`Chessboard`:** A collection of 64 `Box` objects configured as an 8x8 2D array. It is responsible for initializing the board, placing pieces at their default positions via `resetBoard()`, and providing utility methods like checking if a path between two boxes is blocked (important for sliding pieces like Queens, Rooks, and Bishops).

### 2.2 The Pieces
The design leverages the **Strategy** and **Template Method** patterns implicitly via polymorphism.
- **`Piece` (Abstract Class):** The base class for all chess pieces. It holds properties like `white` (boolean to indicate color) and `killed` (boolean). The core of this class is the abstract method `canMove(Chessboard board, Box start, Box end)`.
    - Every specific piece implements its own movement logic by extending `Piece` and overriding `canMove()`.
    - **Classes:** `King`, `Queen`, `Rook`, `Knight`, `Bishop`, `Pawn`.
    - Example: The `Pawn` implements logic for moving forward, double-stepping from the start line, and capturing diagonally. The `Bishop` logic verifies a diagonal path and uses `Chessboard.isPathBlocked()` to ensure no pieces obstruct the path.

### 2.3 Game Flow & State Management
- **`Player`:** Holds player information (name, color side).
- **`Move`:** Represents a single transaction or turn. It records the starting `Box`, ending `Box`, the `pieceMoved`, the `pieceKilled` (if a capture happened), the `Player` who made the move, and the `MoveType` (Normal, Castling, En Passant). This is crucial for maintaining a log (`MoveHistory`) and potentially implementing undo functionality.
- **`ChessGame`:** The central controller (Facade) that glues everything together. It contains:
  - `Player[]` players.
  - `Chessboard` board.
  - `Player` currentTurn.
  - `GameStatus` status (ACTIVE, BLACK_WIN, WHITE_WIN, DRAW, etc.).
  - `List<Move>` moveHistory.
  - `ChessMoveController` controller.
  - `ChessGameView` view.
  It manages the game loop (`play()`), validates inputs, alternates turns, and commits moves.

### 2.4 Control & View (MVC Pattern)
The code embraces a lightweight Model-View-Controller (MVC) architecture:
- **Model:** `Chessboard`, `Box`, `Piece`, `Move`, `GameStatus`, etc.
- **View:** `ChessGameView` is responsible for rendering the board state to the console and displaying move information.
- **Controller:** `ChessMoveController` acts as a service that encapsulates the rule-checking logic. It validates if a proposed move is fundamentally legal before altering the Model.

---

## 3. Design Patterns Applied

1. **Factory Pattern (Implicit):** While not purely extracted into a distinct class, the `Chessboard.resetBoard()` method acts as a Factory, instantiating the correct concrete classes of `Piece` based on board coordinates.
2. **Command Pattern (Conceptual):** The `Move` class represents the Command pattern. Encapsulating a move as an object allows logging, undo/redo (if implemented), and auditing.
3. **Facade Pattern:** `ChessGame` simplifies the interaction between the Client (or Driver) and the complex subsystem (turns, boards, validation, state updates) into a single unified interface.
4. **Strategy Method / Polymorphism:** The abstract `canMove()` in `Piece` allows the controller to blindly call `validateMove()` on any piece without knowing its type, adhering to the Open-Closed Principle.
5. **DRY (Don't Repeat Yourself):** By moving the target square "capture vs move" validation into a `protected` helper method `isValidDestination(Box end)` inside the `Piece` base class, we reduced code duplication across all subclasses. This ensures clean, maintainable code.

---

## 4. Workflows & Step-by-Step Execution

### Scenario: A Player makes a move (e.g., Pawn e2 to e4)
1. **Input Parsing:** `ChessGame` receives the input string "e2 e4" and translates algebraic notation into 0-indexed integer coordinates (x: 1, y: 4 to x: 3, y: 4).
2. **Retrieve Entities:** The game fetches the starting `Box` and ending `Box` from the `Chessboard`.
3. **Pre-Validation:** 
   - Ensure the starting `Box` has a piece.
   - Ensure the piece belongs to the `currentTurn` player.
4. **Rule Validation:** `ChessMoveController` asks the piece: `piece.canMove(board, start, end)`. 
   - The `Pawn` inherently knows if it's white, it can move +2 steps on its first turn, provided the destination is empty and the path is clear.
5. **Execution:** 
   - The captured piece (if any) is identified.
   - The end `Box` is updated to hold the moving `piece`.
   - The start `Box` is set to `null`.
6. **Logging:** A new `Move` object is instantiated and added to `moveHistory`.
7. **Post-Move Logic:** 
   - The turn is switched (`currentTurn` toggles).
   - Base end-game states are checked (e.g., Draw after 50 moves).
8. **View Update:** `ChessGameView` re-renders the board with the updated state.

---

## 5. Potential Enhancements

When discussing this LLD, you can showcase your ability to scale and refine the system by mentioning the following improvements:

1. **Check and Checkmate Validation:** Implement an `isKingInCheck()` algorithm that evaluates the board after a tentative move. If the move leaves their own King in check, it's invalid.
2. **Rule Extensibility:** Use an explicit *Rules Engine* or *Chain of Responsibility* instead of a single `canMove` method for highly complex scenarios (like Castling requiring king not passing through check).
3. **Undo Strategy:** To implement undo, the `Command` pattern (represented by the `Move` class) should have an `undo()` method that reverses the specific piece movements and restores any `pieceKilled`.
4. **Concurrency:** If adapted for an online multiplayer server, `ChessGame` state mutations would need synchronization (e.g., Read/Write locks or `synchronized` blocks) to avoid race conditions when two players send moves simultaneously. 

---

**Summary:** This low-level design is highly modular. It cleanly divides state (Board/Boxes), behavior (Pieces/Controller), and execution flow (ChessGame). It perfectly demonstrates SOLID principles and prepares a scalable foundation for a fully rigid Chess ruleset.
