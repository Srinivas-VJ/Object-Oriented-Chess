package com.example.chess.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String userEmail;
    private String password;
    private String profilePicture;
}