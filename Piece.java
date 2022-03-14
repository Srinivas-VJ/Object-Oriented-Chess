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
	bool isValidMove(int x, int y);

}