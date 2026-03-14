public abstract class Piece {
    private boolean white;
    private boolean killed = false;

    public Piece(boolean white) {
        this.white = white;
    }

    public boolean isWhite() {
        return white;
    }

    public boolean isKilled() {
        return killed;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public abstract boolean canMove(Chessboard board, Box start, Box end);

    /**
     * Common logic for most pieces: a move is only possible if the target box
     * is empty OR occupied by an enemy piece.
     */
    protected boolean isValidDestination(Box end) {
        Piece targetPiece = end.getPiece();
        return targetPiece == null || targetPiece.isWhite() != this.isWhite();
    }

    public abstract String getSymbol();
}