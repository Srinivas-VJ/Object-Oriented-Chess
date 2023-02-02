package com.example.chess.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.convert.DataSizeUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userName;
    private String userEmail;
    private String profilePicture;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private int gamesDrawn = 0;
    private int rating = 600;
}

