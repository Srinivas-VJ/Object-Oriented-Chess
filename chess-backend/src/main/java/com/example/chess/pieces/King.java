package com.example.chess.pieces;
public class King extends Piece {
    public boolean hasMoved = false;
    public King(Colour color) {
        super(color, Variant.KING);
    }

    @Override
    public boolean isValidMove(int r1, int c1, int r2, int c2, Piece[][] board) {
        if ((r2 == r1 + 1 || r2 == r1 - 1) && (c2 == c1 + 1 || c2 == c1 - 1))
            return true;
        else if ((r2 == r1) && (c2 == c1 + 1 || c2 == c1 - 1))
            return true;
        else return (c2 == c1) && (r2 == r1 + 1 || r2 == r1 - 1);
    }
}