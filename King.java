package Pieces;
public class King extends Piece {

	public King(Colour color) {
		super(color, Variant.KING);
	}

	@Override
	public boolean isValidMove(int r, int c, int x, int y) {
		// TODO Auto-generated method stub
		return true;
	}
}