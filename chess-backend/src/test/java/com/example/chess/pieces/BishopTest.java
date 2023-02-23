package com.example.chess.pieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BishopTest {
    private Board boardObj;
    @BeforeEach
    void setUp() {
       boardObj = new Board();
    }

    @Test
    void BishopShouldNotJump() {
        boardObj = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Piece[][] board = boardObj.getGame_board();
        assertFalse(board[7][2].isValidMove(7,2,5,4, board));
        assertFalse(board[7][2].isValidMove(7,2,6,4, board));
        assertFalse(board[7][2].isValidMove(7,2,6,5, board));
        assertFalse(board[7][2].isValidMove(7,2,7,5, board));
    }

    @Test
    void BishopShouldMove() {
        boardObj = new Board("r1bqkbnr/ppp2ppp/8/3p4/3pPB2/3P3P/PPP2PP1/RN1QKB1R b KQkq - 0 6");
        Piece[][] board = boardObj.getGame_board();
        assertEquals(boardObj.getMoveStatus("f4", "e5", Colour.WHITE, "") , 0);
    }
}