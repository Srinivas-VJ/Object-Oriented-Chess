package com.example.chess.Board;

import com.example.chess.pieces.*;
import lombok.Getter;

import java.util.ArrayList;


@Getter
public class Board {
    private Piece[][] game_board;
    // constructor with fen string
    public Board(String fen) {
        this();
        String[] data = fen.split(" ");
        String boardData = data[0];
        String[] rows = boardData.split("/");
        int r = 0;
        for (String row : rows) {
            int c = 0;
            for (int j = 0;j < row.length(); j++) {
                if (Character.isDigit(row.charAt(j)))
                   c += row.charAt(j) - '0';
                else {
                    char ch = row.charAt(j);
                    switch (ch) {
                        case 'k' -> game_board[r][c] = new King(Colour.BLACK);
                        case 'K' -> game_board[r][c] = new King(Colour.WHITE);
                        case 'q' -> game_board[r][c] = new Queen(Colour.BLACK);
                        case 'Q' -> game_board[r][c] = new Queen(Colour.WHITE);
                        case 'b' -> game_board[r][c] = new Bishop(Colour.BLACK);
                        case 'B' -> game_board[r][c] = new Bishop(Colour.WHITE);
                        case 'r' -> game_board[r][c] = new Rook(Colour.BLACK);
                        case 'R' -> game_board[r][c] = new Rook(Colour.WHITE);
                        case 'n' -> game_board[r][c] = new Knight(Colour.BLACK);
                        case 'N' -> game_board[r][c] = new Knight(Colour.WHITE);
                        case 'p' -> game_board[r][c] = new Pawn(Colour.BLACK);
                        case 'P' -> game_board[r][c] = new Pawn(Colour.WHITE);
                        default -> game_board[r][c] = null;
                    }
                    c++;
                }
            }
            r++;
        }
        return ;
    }
    public Board() {
        game_board = new Piece[8][8];
        // making the pawns
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (r == 6)
                    game_board[r][c] = new Pawn(Colour.WHITE);
                else if (r == 1)
                    game_board[r][c] = new Pawn(Colour.BLACK);
            }
        }

        // making the Kings
        game_board[0][4] = new King(Colour.BLACK);
        game_board[7][4] = new King(Colour.WHITE);

        // making the Queens
        game_board[0][3] = new Queen(Colour.BLACK);
        game_board[7][3] = new Queen(Colour.WHITE);

        // making the Rooks
        game_board[0][0] = new Rook(Colour.BLACK);
        game_board[0][7] = new Rook(Colour.BLACK);
        game_board[7][0] = new Rook(Colour.WHITE);
        game_board[7][7] = new Rook(Colour.WHITE);

        // making the Bishops
        game_board[0][2] = new Bishop(Colour.BLACK);
        game_board[0][5] = new Bishop(Colour.BLACK);
        game_board[7][2] = new Bishop(Colour.WHITE);
        game_board[7][5] = new Bishop(Colour.WHITE);

        // making the Knights
        game_board[0][1] = new Knight(Colour.BLACK);
        game_board[0][6] = new Knight(Colour.BLACK);
        game_board[7][1] = new Knight(Colour.WHITE);
        game_board[7][6] = new Knight(Colour.WHITE);
    }
    public int getMoveStatus(String from, String to, Colour player) {
        int r1 = '8' - from.charAt(1);
        int c1 = from.charAt(0) - 'a';
        int r2 = '8' - to.charAt(1);
        int c2 = to.charAt(0) - 'a';
        if (makeMove(r1, c1, r2, c2, player)) {
            // check if game is over
            Colour opponent = player == Colour.BLACK ? Colour.WHITE : Colour.BLACK;
            if (playerHasValidMove(opponent))
                return 0; // code for valid move
            if (isKingInCheck(game_board, opponent))
                return 1; // code for checkmate
            return 2; // code for stalemate
        }
        else {
            // check if castling
//            String castle = fen.split(" ")[2];
            // white
            if (r1 == 7 && c1 == 4 && player.equals(Colour.WHITE)) {
                // queen side
                if (r2 == 7 && c2 == 2)
                    return 0;
                // king side
                if (r2 == 7 && c2 == 6)
                    return 0;
            }
            // black
            else if (r1 == 0 && c1 == 4 && player.equals(Colour.BLACK)) {
                // queen side
                if (r2 == 0 && c2 == 2)
                    return 0;
                // king side
                if (r2 == 0 && c2 == 6)
                    return 0;
            }
            return -1; // code for invalid  move
        }
    }
    public boolean makeMove(int r1, int c1, int r2, int c2, Colour player) {
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
        if (game_board[r1][c1].isValidMove(r1, c1, r2, c2, game_board)) {
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
                    if (game_board[r2][c2].type == Variant.KING || game_board[r2][c2].type == Variant.ROOK)
                        game_board[r2][c2].hasMoved = true;
                }
                return true;
            }
        }
        return false;
    }
    public boolean isKingInCheck(Piece[][] board, Colour player) {
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
                    else if ((board[i][j] != null) && board[i][j].color == Colour.BLACK && board[i][j].isValidMove(i, j, r, c, game_board))
                        return true;
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((board[i][j] != null) && board[i][j].color == Colour.BLACK)
                        continue;
                    else if ((board[i][j] != null) && board[i][j].isValidMove(i, j, r, c, game_board) && board[i][j].color == Colour.WHITE)
                        return true;
                }
            }
        }
        return false;
    }
    public int[][] getValidMoves(int r, int c, Colour player) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (game_board[r][c] != null && game_board[r][c].isValidMove(r, c, i, j, game_board)) {
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
    public boolean playerHasValidMove(Colour Player) {
        Piece[][] board = game_board;
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
                        if (board[r][c].isValidMove(r, c, i, j, game_board)) {
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
        /*
        legacy code :(
        if (isKingInCheck(board, Player)) {
            System.out.println("Checkmate");
            System.exit(0);
        } else {
            System.out.println("Stalemate");
            System.exit(0);
        }
        */
        return false;
    }
    public void printBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = game_board[r][c];
                if (p == null)
                    System.out.print(" . ");
                else {
                    switch (p.type) {
                        case KING -> {
                            if (p.color == Colour.BLACK)
                                System.out.print(" k ");
                            else
                                System.out.print(" K ");
                        }
                        case BISHOP -> {
                            if (p.color == Colour.BLACK)
                                System.out.print(" b ");
                            else
                                System.out.print(" B ");
                        }
                        case KNIGHT -> {
                            if (p.color == Colour.BLACK)
                                System.out.print(" h ");
                            else
                                System.out.print(" H ");
                        }
                        case PAWN -> {
                            if (p.color == Colour.BLACK)
                                System.out.print(" p ");
                            else
                                System.out.print(" P ");
                        }
                        case QUEEN -> {
                            if (p.color == Colour.BLACK)
                                System.out.print(" q ");
                            else
                                System.out.print(" Q ");
                        }
                        case ROOK -> {
                            if (p.color == Colour.BLACK)
                                System.out.print(" r ");
                            else
                                System.out.print(" R ");
                        }
                        default -> System.out.println(" ");
                    }
                }
            }
            System.out.println();
        }
    }
}