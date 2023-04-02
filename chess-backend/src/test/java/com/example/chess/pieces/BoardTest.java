package com.example.chess.pieces;

import com.example.chess.Board.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("validation")
public class BoardTest {
    private static Piece[][] board;
    @BeforeAll
    public static void initBoard() {
        board = new Piece[8][8];
            // making the pawns
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (r == 6)
                        board[r][c] = new Pawn(Colour.WHITE);
                    else if (r == 1)
                        board[r][c] = new Pawn(Colour.BLACK);
                }
            }
            // making the Kings
            board[0][4] = new King(Colour.BLACK);
            board[7][4] = new King(Colour.WHITE);

            // making the Queens
            board[0][3] = new Queen(Colour.BLACK);
            board[7][3] = new Queen(Colour.WHITE);

            // making the Rooks
            board[0][0] = new Rook(Colour.BLACK);
            board[0][7] = new Rook(Colour.BLACK);
            board[7][0] = new Rook(Colour.WHITE);
            board[7][7] = new Rook(Colour.WHITE);

            // making the Bishops
            board[0][2] = new Bishop(Colour.BLACK);
            board[0][5] = new Bishop(Colour.BLACK);
            board[7][2] = new Bishop(Colour.WHITE);
            board[7][5] = new Bishop(Colour.WHITE);

            // making the Knights
            board[0][1] = new Knight(Colour.BLACK);
            board[0][6] = new Knight(Colour.BLACK);
            board[7][1] = new Knight(Colour.WHITE);
            board[7][6] = new Knight(Colour.WHITE);
        }

    @Test
    @DisplayName("Create board from initial fen")
    public void CreateBoardFromFenTest() {
        Board boardObj = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Piece[][] actual = boardObj.getGame_board();
        assertArrayEquals(actual, board);
    }
}
