
public class Board {
	//represent the actual game state 
	static Piece game_board[][];
	
	// a temporary game state to test things
	static Piece temp_board[][];
	
	
	public static void main(String args[])
	{
		Board b = new Board();
		b.printBoard();
	}
	
	
	Board()
	{
		temp_board = new Piece[8][8];
		initBoard();
	}
	
	
	void initBoard()
	{
		// making the pawns
		for (int r = 0; r < 8; r++)
		{
			for(int c = 0; c < 8; c++)
			{
		
				if (r == 6)
					temp_board[r][c] = new Pawn(Colour.WHITE);
				else if (r == 1)
					temp_board[r][c] = new Pawn(Colour.BLACK);
				else
					temp_board[r][c] = null;
	
			}
		}
		
		// making the Kings
		temp_board[0][4] = new King(Colour.BLACK);
		temp_board[7][4] = new King(Colour.WHITE);
		
		// making the Queens
		temp_board[0][3] = new Queen(Colour.BLACK);
		temp_board[7][3] = new Queen(Colour.WHITE);
		
		// making the Rooks
		temp_board[0][0] = new Rook(Colour.BLACK);
		temp_board[0][7] = new Rook(Colour.BLACK);
		temp_board[7][0] = new Rook(Colour.WHITE);
		temp_board[7][7] = new Rook(Colour.WHITE);
		
		// making the Bishops
		temp_board[0][2] = new Bishop(Colour.BLACK);
		temp_board[0][5] = new Bishop(Colour.BLACK);
		temp_board[7][2] = new Bishop(Colour.WHITE);
		temp_board[7][5] = new Bishop(Colour.WHITE);
		
		// making the Knights
		temp_board[0][1] = new Knight(Colour.BLACK);
		temp_board[0][6] = new Knight(Colour.BLACK);
		temp_board[7][1] = new Knight(Colour.WHITE);
		temp_board[7][6] = new Knight(Colour.WHITE);
		
	}
	
	void printBoard()
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
