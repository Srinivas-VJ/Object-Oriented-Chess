package pieces;

import board.Board;

public class Rook extends Piece {

	public Rook(Colour color) {
		super(color, Variant.ROOK);
	}

	@Override
	public boolean isValidMove(int r1, int c1, int r2, int c2) {
		if (this.isSelfSabotage(r1, c1, r2, c2))
			return false;
		Piece board[][] = Board.getBoard();
		if (r1 == r2)
		{
			if (c1>c2)
			{
				for(int i = (c1-1);i>c2;i--)
				{
					if (board[r2][i] == null) continue;
					return false;
				}
			}
			else if (c2>c1)
			{
				for(int i = (c1+1);i<c2;i++)
				{
					if (board[r2][i] == null) continue;
					return false;
				}
			}
			else
				return false;
		}
		else if(c1 == c2)
		{
			if (r1>r2)
			{
				for(int i = (r1-1);i>r2;i--)
				{
					if (board[i][c2] == null) continue;
					return false;
				}
			}
			else if (r2>r1)
			{
				for(int i = (r1+1);i<r2;i++)
				{
					if (board[i][c2] == null) continue;
					return false;
				}
			}
			else
				return false;
		}
		else
			return false;
		return true;
	}
}