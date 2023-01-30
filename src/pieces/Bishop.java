package pieces;
import board.Board;
public class Bishop extends Piece {

    public Bishop(Colour color) {
        super(color, Variant.BISHOP);
    }

    @Override
    public boolean isValidMove(int r1, int c1, int r2, int c2) {
    	if (this.isSelfSabotage(r1, c1, r2, c2))
    		return false;
        Piece board[][] = Board.getBoard();
        if (r2 > r1 && c2 > c1) {
            if ((r2 - r1) == (c2 - c1)) {
                for (int i = r1 + 1, j = c1 + 1; i < r2; i++, j++) {
                    if (board[i][j] == null)
                        continue;
                    return false;
                }
            } else
                return false;
        } else if (r2 > r1 && c2 < c1) {
            if ((r2 - r1) == (c1 - c2)) {
                for (int i = r1 + 1, j = c1 - 1; i < r2; i++, j--) {
                    if (board[i][j] == null)
                        continue;
                    return false;
                }
            } else
                return false;
        } else if (r1 > r2 && c2 > c1) {
            if ((r1 - r2) == (c2 - c1)) {
                for (int i = r1 - 1, j = c1 + 1; j < c2; i--, j++) {
                    if (board[i][j] == null)
                        continue;
                    return false;
                }
            } else
                return false;
        } else if (r2 < r1 && c2 < c1) {
            if ((r1 - r2) == (c1 - c2)) {
                for (int i = r1 - 1, j = c1 - 1; i > r2; i--, j--) {
                    if (board[i][j] == null)
                        continue;
                    return false;
                }
            } else
                return false;
        } else
            return false;
        return true;
    }
}