package Pieces;
import GameBoard.Board;
public class Pawn extends Piece {
	public Pawn(Colour color) {
		super(color, Variant.PAWN);
	}

	@Override
	public boolean isValidMove(int r1, int c1, int r2, int c2) {
		// TODO Auto-generated method stub
		// Piece board[][] = Board.getBoard();
		// boolean res;
		// if (c1 == c2 )
		// {
		// 	if ((r1 == 6) && (board[r2][c2] == null) && (board[r1 - 1][c2] == null) &&  (r2==4 || r2==5) && (board[r1][c1].color == Colour.WHITE))
		// 		return true;			
		// 	else if ((r2+1) == r1 && (board[r2][c2] == null) && (board[r1][c1].color == Colour.WHITE))
		// 		return true;
		// 	if ((r1 == 1) && (board[r2][c2] == null) && (board[r1 + 1][c2] == null) &&  (r2==3 || r2==2)&& (board[r1][c1].color == Colour.BLACK))
            	// 		return true;
        	// 	else if ((r2-1) == r1 && (board[r2][c2] == null)&& (board[r1][c1].color == Colour.WHITE))
           	// 		return true;
		// }
		// else if ((board[r2][c2].color == Colour.BLACK) && (r2 == (r1 - 1)) && (c2 == (c1 - 1) ||  c2 == (c1+1)) && (board[r1][c1].color == Colour.WHITE))
		// 	return true;
		// else if ((board[r2][c2].color == Colour.WHITE) && (r2 == (r1 + 1)) && (c2 == (c1 - 1) ||  c2 == (c1+1) && (board[r1][c1].color == Colour.BLACK)))
        	// 	return true;
		
		// return false;
		return true;
	}

}