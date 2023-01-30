package pieces;
import board.Board;
public class Queen extends Piece {

	public Queen(Colour color) {
		super(color, Variant.QUEEN);
	}

	@Override
	public boolean isValidMove(int r, int c, int x, int y) {
		Piece board[][] = Board.getBoard();
		Bishop b = new Bishop(board[r][c].color);
		Rook rk = new Rook(board[r][c].color);
		return b.isValidMove(r, c, x, y) || rk.isValidMove(r, c, x, y);
	}
}