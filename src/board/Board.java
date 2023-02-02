package board;

import java.util.ArrayList;

import pieces.*;

public class Board {
	

    //represent the actual game state 
    private static Piece game_board[][];

    // a temporary game state to test things
    private static Piece temp_board[][];
    
    static {
		temp_board = new pieces.Piece[8][8];
        game_board = new pieces.Piece[8][8];
        initBoard(temp_board);
        initBoard(game_board);
	}

    public static boolean makeMove(int r1, int c1, int r2, int c2, Colour player) {
        //empty square
        if (game_board[r1][c1] == null) {
            System.out.println("Input square cannot be empty");
            return false;
        }

        //not the right player
        if (game_board[r1][c1].color != player) {
            System.out.println("You cannot move the opponents piece silly");
            return false;
        }

        //not a valid move
        if (game_board[r2][c2] != null && game_board[r2][c2].color == player) {
            System.out.println("You cannot self-sabotage just resign already");
            return false;
        }

        //move the piece if valid move returned");		
        if (game_board[r1][c1].isValidMove(r1, c1, r2, c2)) {
            Piece temp = game_board[r2][c2];
            game_board[r2][c2] = game_board[r1][c1];
            game_board[r1][c1] = null;
            if (isKingInCheck(game_board, player)) {
                System.out.println("This will put you in check");
                // undo the move
                game_board[r1][c1] = game_board[r2][c2];
                game_board[r2][c2] = temp;
                return false;
            } else {
                player = player == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
                if (playerHasValidMove(player))
		{
                    player = player == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
	            if (game_board[r2][c2].type == Variant.KING || game_board[r2][c2].type == Variant.ROOK) 
			game_board[r2][c2].hasMoved = true;
		}
                return true;
            }

        }

        return false;

    }

    public static Piece[][] getBoard() {
        return game_board;
    }

    public static boolean isKingInCheck(Piece board[][], Colour player) {
        //check if the king is in check

        // get king position
        int r = -1, c = -1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((board[i][j] != null) && (board[i][j].type == Variant.KING) && (board[i][j].color == player)) {
                    r = i;
                    c = j;
                    i = 8;
                    break;
                }
            }
        }

        //check if any of the other players pieces can attack the king
        if (player == Colour.WHITE) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((board[i][j] == null) || board[i][j].color == Colour.WHITE)
                        continue;
                    else if ((board[i][j] != null) && board[i][j].color == Colour.BLACK && board[i][j].isValidMove(i, j, r, c))
                        return true;
                }
            }
            return false;
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((board[i][j] != null) && board[i][j].color == Colour.BLACK)
                        continue;
                    else if ((board[i][j] != null) && board[i][j].isValidMove(i, j, r, c) && board[i][j].color == Colour.WHITE)
                        return true;
                }
            }
            return false;
        }
    }
    public static int[][] getValidMoves(int r, int c, Colour player) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        for (int i = 0; i < 8; i++) 
            for (int j = 0; j < 8; j++) 
                if (game_board[r][c] != null && game_board[r][c].isValidMove(r, c, i, j)) {
                    Piece temp = game_board[i][j];
                    game_board[i][j] = game_board[r][c];
                    game_board[r][c] = null;	
                    if (!isKingInCheck(game_board, player)) {
						int[] move = new int[2];
						move[0] = i;
						move[1] = j;
						moves.add(move);
                    }
                    // undo the move
                    game_board[r][c] = game_board[i][j];
                    game_board[i][j] = temp;
                }
        return moves.toArray(new int[moves.size()][]);
    }

    public static boolean playerHasValidMove(Colour Player) {
        Piece board[][] = game_board;
        Piece temp;
        int i, j;
        int r = -1, c = -1;
        for (r = 0; r < 8; r++) {
            for (c = 0; c < 8; c++) {
                if ((board[r][c] == null) || board[r][c].color != Player)
                    continue;
                for (i = 0; i < 8; i++)
                    for (j = 0; j < 8; j++) {
                        if (board[i][j] != null && board[i][j].color == Player)
                            continue;
                        if (board[r][c].isValidMove(r, c, i, j)) {
                            temp = board[i][j];
                            board[i][j] = board[r][c];
                            board[r][c] = null;
                            if (!isKingInCheck(board, Player)) {
                                board[r][c] = board[i][j];
                                board[i][j] = temp;
                                return true;
                            }
                            board[r][c] = board[i][j];
                            board[i][j] = temp;
                        }
                    }
            }
        }
        if (isKingInCheck(board, Player)) {
            System.out.println("Checkmate");
            System.exit(0);
        } else {
            System.out.println("Stalemate");
            System.exit(0);
        }
        return false;
    }

    private static void initBoard(Piece b[][]) {
        // making the pawns
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (r == 6)
                    b[r][c] = new Pawn(Colour.WHITE);
                else if (r == 1)
                    b[r][c] = new Pawn(Colour.BLACK);
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

    public static void printBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = temp_board[r][c];
                if (p == null)
                    System.out.print(" . ");
                else {
                    switch (p.type) {
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