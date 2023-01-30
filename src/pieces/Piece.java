package pieces;

import board.Board;

abstract public class Piece {
	public Colour color;
	public Variant type;
	public boolean hasMoved = false;

	public Piece(Colour color, Variant type)
	{
		this.color = color;
		this.type = type;
	}
	public boolean isSelfSabotage(int r, int c , int i , int j) {
		Piece[][] board = Board.getBoard();
		if (board[i][j] != null && board[i][j].color == this.color)
			return true;
		return false;
	}

	public abstract boolean isValidMove(int r, int c, int x, int y);
}

