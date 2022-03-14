enum Color {
	BLACK,
	WHITE;
}

enum Variant {
	PAWN,
	ROOK,
	KNIGHT,
	BISHOP,
	QUEEN,
	KING;
}

abstract class Piece {
	Color color;
	Variant type;
	int row, col;

	Piece(Color color, int r, int c, Variant type)
	{
		this.color = color;
		this.row = r;
		this.col = c;
		this.type = type;
	}

	abstract boolean isValidMove(int x, int y);
}	