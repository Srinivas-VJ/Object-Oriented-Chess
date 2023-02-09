package com.example.chess.exception;

public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException() {
        super("This move is invalid !");
    }
}
