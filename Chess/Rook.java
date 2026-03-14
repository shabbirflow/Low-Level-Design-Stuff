public class Rook extends Piece {
    public Rook(boolean white) {
        super(white);
    }

    public boolean canMove(Chessboard board, Box start, Box end) {
        int dx = start.getX() - end.getX();
        int dy = start.getY() - end.getY();
        if ((dx == 0 || dy == 0) && isValidDestination(end)) {
            return !Chessboard.isPathBlocked(board, start, end);
        }
        return false;
    }

    public String getSymbol() {
        return isWhite() ? "♖" : "♜";
    }
}