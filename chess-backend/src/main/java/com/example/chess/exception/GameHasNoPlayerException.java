package com.example.chess.exception;

public class GameHasNoPlayerException extends RuntimeException {
    public  GameHasNoPlayerException(){
        super("Game requires 2 players to create");
    }
}
