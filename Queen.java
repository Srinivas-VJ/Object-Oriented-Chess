package Pieces;
public class Queen extends Piece {

	public Queen(Colour color) {
		super(color, Variant.QUEEN);
	}

	@Override
	public boolean isValidMove(int r, int c, int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}
}