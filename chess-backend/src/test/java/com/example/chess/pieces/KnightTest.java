
package com.example.chess.pieces;

import com.example.chess.Board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class KnightTest {
    private Board boardObj;
    @BeforeEach
    void setUp() {
        boardObj = new Board();
    }

    @Test
    void knightShouldJumpCorrectly() {
        boardObj = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Piece[][] board = boardObj.getGame_board();
        assertTrue(board[0][1].isValidMove(0,1,2,0, board));
        assertTrue(board[0][1].isValidMove(0,1,2,2, board));
    }

}