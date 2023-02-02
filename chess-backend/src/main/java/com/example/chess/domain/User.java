package com.example.chess.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 69)
    @NotBlank(message = "username is required")
    private String userName;
    @Email(message = "invalid email address")
    private String userEmail;
    private String profilePicture;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private int gamesDrawn = 0;
    private int rating = 600;
}

