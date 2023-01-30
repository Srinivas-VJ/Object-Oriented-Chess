package Pieces;
abstract public class Piece {
	public Colour color;
	public Variant type;
	public boolean hasMoved = false;

	public Piece(Colour color, Variant type)
	{
		this.color = color;
		this.type = type;
	}

	abstract public boolean isValidMove(int r, int c, int x, int y);
}

