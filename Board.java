import Pieces.Piece;
import Pieces.Pawn;
import Pieces.Colour;
import Pieces.Variant;
import Pieces.Rook;
import Pieces.Bishop;
import Pieces.King;
import Pieces.Queen;
import Pieces.Knight;

public class Board {
	// singleton for board
	private static boolean hasBeenInitialized = false;

	//represent the actual game state 
	static Piece game_board[][];
	
	// a temporary game state to test things
	static Piece temp_board[][];
	

	
	private Board()
	{
		temp_board = new Pieces.Piece[8][8];
		game_board = new Pieces.Piece[8][8];
		initBoard(temp_board);
		initBoard(game_board);
	}
	
	public static Piece[][] getBoard()
	{
		if (!hasBeenInitialized)
		{
			Board b = new Board();
			hasBeenInitialized = true;
			return game_board;
		}
		return game_board;
	}
		
	public static void main(String args[])
	{
		Board b = new Board();
		b.printBoard();
	}
	
	
	private void initBoard(Piece b[][])
	{
		// making the pawns
		for (int r = 0; r < 8; r++)
		{
			for(int c = 0; c < 8; c++)
			{
		
				if (r == 6)
					b[r][c] = new Pawn(Colour.WHITE);
				else if (r == 1)
					b[r][c] = new Pawn(Colour.BLACK);
				else
					b[r][c] = null;
	
			}
		}
		
		// making the Kings
		b[0][4] = new King(Colour.BLACK);
		b[7][4] = new King(Colour.WHITE);
		
		// making the Queens
		b[0][3] = new Queen(Colour.BLACK);
		b[7][3] = new Queen(Colour.WHITE);
		
		// making the Rooks
		b[0][0] = new Rook(Colour.BLACK);
		b[0][7] = new Rook(Colour.BLACK);
		b[7][0] = new Rook(Colour.WHITE);
		b[7][7] = new Rook(Colour.WHITE);
		
		// making the Bishops
		b[0][2] = new Bishop(Colour.BLACK);
		b[0][5] = new Bishop(Colour.BLACK);
		b[7][2] = new Bishop(Colour.WHITE);
		b[7][5] = new Bishop(Colour.WHITE);
		
		// making the Knights
		b[0][1] = new Knight(Colour.BLACK);
		b[0][6] = new Knight(Colour.BLACK);
		b[7][1] = new Knight(Colour.WHITE);
		b[7][6] = new Knight(Colour.WHITE);
		
	}
	
	public void printBoard()
	{
		for (int r = 0; r < 8; r++)
		{
			for (int c = 0; c < 8; c++)
			{
				Piece p = temp_board[r][c];
				if (p == null)
					System.out.print(" . ");
				else
				{
					switch(p.type)
					{
					case KING:
						if (p.color == Colour.BLACK)
							System.out.print(" k ");
						else
							System.out.print(" K ");
						break;
					case BISHOP:
						if (p.color == Colour.BLACK)
							System.out.print(" b ");
						else
							System.out.print(" B ");
						break;
					case KNIGHT:
						if (p.color == Colour.BLACK)
							System.out.print(" h ");
						else
							System.out.print(" H ");
						break;
					case PAWN:
						if (p.color == Colour.BLACK)
							System.out.print(" p ");
						else
							System.out.print(" P ");
						break;
					case QUEEN:
						if (p.color == Colour.BLACK)
							System.out.print(" q ");
						else
							System.out.print(" Q ");
						break;
					case ROOK:
						if (p.color == Colour.BLACK)
							System.out.print(" r ");
						else
							System.out.print(" R ");
						break;
					default:
						System.out.println(" ");
						
					}
					
				}
					
				
			}
			System.out.println("");
		}
	}
}

 