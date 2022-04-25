package Pieces;
public class King extends Piece {

	public King(Colour color) {
		super(color, Variant.KING);
	}

	@Override
	public boolean isValidMove(int r1, int c1, int r2, int c2) {
		// TODO Auto-generated method stub
		// if (r2 == r1 && !(c2 == c1 + 1 || c2 == c1 - 1))
		// 	if (isCastle(Board,r1,c1,r2,c2))
		// 	{
		// 		castle(r1,c1,r2,c2);
		// 		return 1;
		// 	}
		if ((r2 == r1 + 1 || r2 == r1 -1) &&  (c2 == c1 + 1 || c2 == c1 -1))
			return true;
		else if ((r2 == r1) && (c2 == c1 + 1 || c2 == c1 - 1) )
			return true;
		else if ((c2 == c1) && (r2 == r1 + 1 || r2 == r1 - 1) )
			return true;
		else
			return false;
	}
}