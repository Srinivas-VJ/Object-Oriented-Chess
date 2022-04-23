package Pieces;
public class Pawn extends Piece {
	public Pawn(Colour color) {
		super(color, Variant.PAWN);
	}

	@Override
	public boolean isValidMove(int r, int c, int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

}