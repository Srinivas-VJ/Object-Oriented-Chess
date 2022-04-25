package Pieces;
public class Knight extends Piece {

	public Knight(Colour color) {
		super(color, Variant.KNIGHT);
	}

	@Override
	public boolean isValidMove(int r1, int c1, int r2, int c2) {
		// TODO Auto-generated method stub
		if (((r2 == (r1+2)) || (r2 == (r1-2))) && ((c2 == (c1+1)) || (c2 == (c1-1))))
			return true;
		else if (((c2 == (c1+2)) || (c2 == (c1-2))) && ((r2 == (r1+1)) || (r2 == (r1-1))))
			return true;
		else
			return false;
	}
}