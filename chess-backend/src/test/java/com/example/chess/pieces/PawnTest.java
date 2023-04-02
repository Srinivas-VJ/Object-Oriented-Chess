package com.example.chess.pieces;

import com.example.chess.Board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PawnTest {
    private Board boardObj;
    @BeforeEach
    void setUp() {
        boardObj = new Board();
    }

    @Test
    void pawnShouldMoveCorrectly() {
        boardObj = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Piece[][] board = boardObj.getGame_board();
        assertTrue(board[1][1].isValidMove(1,1,2,1, board));
        assertTrue(board[1][1].isValidMove(1,1,3,1, board));
        assertTrue(board[6][1].isValidMove(6,1,5,1, board));
        assertTrue(board[6][1].isValidMove(6,1,4,1, board));
    }

}