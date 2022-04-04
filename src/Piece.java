
abstract class Piece {
	Colour color;
	Variant type;

	Piece(Colour color, Variant type)
	{
		this.color = color;
		this.type = type;
	}

	abstract boolean isValidMove(int r, int c, int x, int y);
}


