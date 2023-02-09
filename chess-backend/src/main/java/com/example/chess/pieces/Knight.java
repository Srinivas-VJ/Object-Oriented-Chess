package com.example.chess.pieces;

public class Knight extends Piece {

    public Knight(Colour color) {
        super(color, Variant.KNIGHT);
    }

    @Override
    public boolean isValidMove(int r1, int c1, int r2, int c2, Piece[][] board) {
        if (this.isSelfSabotage(r1, c1, r2, c2, board))
            return false;
        if (((r2 == (r1+2)) || (r2 == (r1-2))) && ((c2 == (c1+1)) || (c2 == (c1-1))))
            return true;
        else return ((c2 == (c1 + 2)) || (c2 == (c1 - 2))) && ((r2 == (r1 + 1)) || (r2 == (r1 - 1)));
    }
}