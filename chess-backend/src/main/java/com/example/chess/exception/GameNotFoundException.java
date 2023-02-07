package com.example.chess.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException() {
        super("Game with the give gameId does not exist !");
    }
}
