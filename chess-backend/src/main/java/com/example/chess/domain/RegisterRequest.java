package com.example.chess.domain;

import lombok.Data;
@Data
public class RegisterRequest {
    private String username;
    private String userEmail;
    private String password;
    private String profilePicture;
}