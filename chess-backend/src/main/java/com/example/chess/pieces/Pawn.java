package com.example.chess.pieces;

public class Pawn extends Piece {
    public Pawn(Colour color) {
        super(color, Variant.PAWN);
    }

    @Override
    public boolean isValidMove(int r1, int c1, int r2, int c2, Piece[][] board) {
        if (this.isSelfSabotage(r1, c1, r2, c2, board))
            return false;

        //same block
        if (r1 == r2 && c1 == c2) {
            return false;
        }

        if (board[r1][c1].color == Colour.WHITE)
        {
            boolean res;
            if (c1 == c2 )
            {
                // allow two steps first move
                if ((r1 == 6) && (board[r2][c2] == null) && (board[r1 - 1][c2] == null) &&  (r2==4 || r2==5))
                    res = true;
                    // typical pawn move
                else res = (r2 + 1) == r1 && (board[r2][c2] == null);
            }
            //capture
            else if ((board[r2][c2] != null) && (board[r2][c2].color == Colour.BLACK) && (r2 == (r1 - 1)) && (c2 == (c1 - 1) ||  c2 == (c1+1)))
                res = true;
            else
                res = false;

            return res;

        }
        // same thing for black code could be optimised
        else
        {
            boolean res;
            if (c1 == c2 )
            {
                // allow two steps first move
                if ((r1 == 1) && (board[r2][c2] == null) && (board[r1 + 1][c2] == null) &&  (r2==3 || r2==2))
                    res = true;
                    // typical pawn move
                else res = (r2 - 1) == r1 && (board[r2][c2] == null);
            }
            // capture
            else res = (board[r2][c2] != null) && (board[r2][c2].color == Colour.WHITE) && (r2 == (r1 + 1)) && (c2 == (c1 - 1) || c2 == (c1 + 1));

            return res;
        }
    }

}