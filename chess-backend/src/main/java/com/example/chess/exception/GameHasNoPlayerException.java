package com.example.chess.exception;

public class GameHasNoPlayerException extends RuntimeException {
    public  GameHasNoPlayerException(){
        super("Game requires at least 1 player to initiate");
    }
}
