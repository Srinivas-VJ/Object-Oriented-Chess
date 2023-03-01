package com.example.chess.pieces;


import java.util.Objects;

abstract public class Piece {
    public Colour color;
    public Variant type;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return color == piece.color && type == piece.type;
    }
    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    public boolean hasMoved = false;

    public Piece(Colour color, Variant type)
    {
        this.color = color;
        this.type = type;
    }
    public boolean isSelfSabotage(int r, int c , int i , int j, Piece[][] board) {
        return board[i][j] != null && board[i][j].color == this.color;
    }
    public abstract boolean isValidMove(int r, int c, int x, int y, Piece[][] board);
}