package com.example.chess.exception;

import com.example.chess.domain.User;

public class UserNameAlreadyExistsException extends RuntimeException {
    UserNameAlreadyExistsException() {
        super("username already exists");
    }

}
