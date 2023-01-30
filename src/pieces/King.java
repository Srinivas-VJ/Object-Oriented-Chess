package pieces;
import board.Board;
public class King extends Piece {
    public boolean hasMoved = false;
    public King(Colour color) {
        super(color, Variant.KING);
    }

    @Override
    public boolean isValidMove(int r1, int c1, int r2, int c2) {
        // TODO Auto-generated method stub
        if (r2 == r1 && !(c2 == c1 + 1 || c2 == c1 - 1))
        {
            if (isCastle( r1, c1, r2, c2)) {
//                castle(r1, c1, r2, c2);
                return true;
            }
            return false;
        }
        if ((r2 == r1 + 1 || r2 == r1 - 1) && (c2 == c1 + 1 || c2 == c1 - 1))
            return true;
        else if ((r2 == r1) && (c2 == c1 + 1 || c2 == c1 - 1))
            return true;
        else if ((c2 == c1) && (r2 == r1 + 1 || r2 == r1 - 1))
            return true;
        else
            return false;
    }
    boolean isCastle(int r1, int c1, int r2, int c2) {
        Piece board[][] = Board.getBoard();
        if (Board.isKingInCheck(board, board[r1][c1].color))
            return false;
        if (r1 == 0 && c1 == 4) {
            if (board[r1][c1].hasMoved)
                return false;

            if ((c2 == 6) && (board[0][7] != null) && (board[0][7].type == Variant.ROOK)) {
                if (board[0][7].hasMoved)
                    return false;

                for (int i = c1 + 1; i <= 6; i++) {
                    if (board[r1][i] == null) {
                        continue;
                    }
                    return false;
                }
            }
            else if ((c2 == 2) &&( board[0][0] != null) && (board[0][0].type == Variant.ROOK) && (!board[0][0].hasMoved)){
                for (int i = c1 - 1; i >= 1; i--) {
                    if (board[r1][i] == null) {
                        continue;
                    }
                    return false;
                }
            }

        } else if (r1 == 7 && c1 == 4) {
            if (board[r1][c1].hasMoved)
                return false;
            if ((c2 == 6) && (board[7][7] != null )&& (board[7][7].type == Variant.ROOK) && (!board[7][7].hasMoved)){
                for (int i = c1 + 1; i <= 6; i++) {
                    if (board[r1][i] == null) {
                        continue;
                    }
                    return false;
                }
            } else if ((c2 == 2) &&( board[7][0] != null )&& (board[7][0].type == Variant.ROOK) && (!board[7][0].hasMoved)){
                for (int i = c1 - 1; i >= 1; i--) {
                    if (board[r1][i] == null) {
                        continue;
                    }
                    return false;
                }
            }
        }
        return true;
    }
    void castle(int r1, int c1, int r2, int c2) {
        Piece board[][] = Board.getBoard();
        if (r1 == 0) {
            if (c2 == 6) {
                board[0][7] = null;
                board[0][5] = new Rook(Colour.BLACK);
            }
            if (c2 == 2) {
                board[0][0] = null;
                board[0][3] = new Rook(Colour.BLACK);
            }
        } else {
            if (c2 == 6) {
                board[7][7] = null;
                board[7][5] = new Rook(Colour.WHITE);
            }
            if (c2 == 2) {
                board[7][0] = null;
                board[7][3] = new Rook(Colour.WHITE);
            }
        }
    }
}
