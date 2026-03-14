# Implementing Advanced Chess Features: A Technical Guide

This guide explains how to transition your basic Chess LLD into a production-grade system by implementing the "Potential Enhancements" discussed in our main LLD document.

---

## 1. Check and Checkmate Validation

This is the most critical logic in Chess. It prevents a player from making a move that exposes their King and detects when the game is over.

### The "Look-Ahead" Strategy
To implement this, you need a method that "simulates" a move before committing it.

**Algorithm for `isValidMoveWithCheck()`:**
1.  **Tentative Move:** Temporarily move the piece on the board and remove any captured piece.
2.  **Find King:** Locate the `King` piece of the current player.
3.  **Scan Threats:** Iterate through all of the *opponent's* remaining pieces. For each piece, check if it `canMove()` to the King's square.
4.  **Rollback:** Move the piece back to its original square and restore any captured piece.
5.  **Verdict:** If any opponent piece could reach the King, the move is illegal.

### Detecting Checkmate
A player is in checkmate if:
1.  Their King is currently in Check.
2.  **AND** There are ZERO legal moves available for any of their pieces that result in the King not being in Check.

```java
public boolean isCheckmate(Player player) {
    if (!isKingInCheck(player)) return false;
    
    // Try every possible move for every piece the player owns
    for (Piece p : player.getPieces()) {
        for (Box destination : board.getBoxes()) {
            if (p.canMove(board, p.getCurrentBox(), destination)) {
                if (!simulatedMoveLeavesKingInCheck(p, destination)) {
                    return false; // Found at least ONE escape move
                }
            }
        }
    }
    return true; // No moves can save the King
}
```

---

## 2. Rule Extensibility (Rules Engine Pattern)

As the game grows (adding Castling, En Passant, etc.), the `canMove()` method can become a "spaghetti" of `if-else` statements. Use a **Rules Engine** or **Chain of Responsibility**.

### Implementation Idea:
Instead of putting all logic in the piece, create a `MoveRule` interface.

```java
public interface MoveRule {
    boolean validate(Chessboard board, Box start, Box end);
}

// Example Implementation
public class PathClearRule implements MoveRule {
    public boolean validate(...) {
        // Only applies to sliding pieces
        if (start.getPiece() instanceof Queen) {
            return !Chessboard.isPathBlocked(...);
        }
        return true;
    }
}
```

The `ChessMoveController` then maintains a `List<MoveRule>` and iterates through them. This makes the system **Open for Extension but Closed for Modification** (Solid Principle).

---

## 3. Undo Strategy (Command Pattern)

The **Command Pattern** is the standard way to implement Undo/Redo.

### Steps:
1.  **Encapsulate State:** The `Move` class already acts as a Command. It stores:
    *   Starting position.
    *   Ending position.
    *   The piece moved.
    *   The piece killed (if any).
2.  **Add `unexecute()` Method:**
    ```java
    public void undo() {
        startBox.setPiece(pieceMoved);
        endBox.setPiece(pieceKilled); // Restores the captured piece or sets to null
        if (pieceKilled != null) pieceKilled.setKilled(false);
    }
    ```
3.  **History Stack:** Use a `Stack<Move>` in your `ChessGame` class. When a user clicks "Undo", pop the last move and call `undo()`.

---

## 4. Concurrency & Multiplayer

If you translate this LLD to an online server, multiple users might try to interact with the same `ChessGame` object.

### The Locking Strategy
Use **ReentrantReadWriteLocks** for high performance:
*   **Read Lock:** Used when `view.showBoard()` is called. Multiple people can view the board at once.
*   **Write Lock:** Used when `playMove()` is called. Only one move can be processed at a time to prevent "Race Conditions" (e.g., two players capturing the same piece at the exact same microsecond).

```java
private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

public void playMove(...) {
    lock.writeLock().lock();
    try {
        // ... execute move logic ...
    } finally {
        lock.writeLock().unlock();
    }
}
```

---

## 5. Castling (Special Case Implementation)

To implement Castling, you need to add persistent state to the `Piece` class:
*   `private boolean hasMoved = false;`

**Logic for `isValidCastling()`**:
1.  King hasn't moved.
2.  Rook hasn't moved.
3.  No pieces are between them (`isPathBlocked == false`).
4.  King is not currently in check.
5.  King does not pass through a square attacked by an enemy.

---

**Technical Tip:** When discussing design changes, you can highlight that the modular system allows updating specific layers (Controller or Model) without affecting others. This demonstrates a deep understanding of architectural boundaries.
