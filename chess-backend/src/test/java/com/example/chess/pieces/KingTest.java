package com.example.chess.pieces;

import com.example.chess.Board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class KingTest {
    private Board boardObj;

    @BeforeEach
    void setUp() {
        boardObj = new Board();
    }

    @Test
    void kingShouldNotJump() {
        boardObj = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Piece[][] board = boardObj.getGame_board();
        assertFalse(board[0][4].isValidMove(0,4,6,5, board));
        assertFalse(board[0][4].isValidMove(0,4,6,4, board));
        assertFalse(board[0][4].isValidMove(0,4,5,4, board));
        assertFalse(board[0][4].isValidMove(0,4,7,5, board));
    }

}