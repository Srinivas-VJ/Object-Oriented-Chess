package com.example.chess.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("user does not exist!");
    }
}
