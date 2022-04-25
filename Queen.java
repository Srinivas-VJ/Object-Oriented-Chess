package Pieces;
import GameBoard.Board;
public class Queen extends Piece {

	public Queen(Colour color) {
		super(color, Variant.QUEEN);
	}

	@Override
	public boolean isValidMove(int r, int c, int x, int y) {
		// TODO Auto-generated method stub
		Piece board[][] = Board.getBoard();
		if (board[r][c].color == Colour.BLACK)
		{
			Bishop b = new Bishop(Colour.BLACK);
			Rook rk = new Rook(Colour.BLACK);
			return b.isValidMove(r, c, x, y) || rk.isValidMove(r, c, x, y);
		}
		else
		{
			Bishop b = new Bishop(Colour.WHITE);
			Rook rk = new Rook(Colour.WHITE);
			return b.isValidMove(r, c, x, y) || rk.isValidMove(r, c, x, y);
		}
	}
}