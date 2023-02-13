package com.example.chess.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Size(min = 3, max = 69, message = "User names must have a minimum size of 3 and a maximum size of 69")
    @NotNull(message = "User name is a required field")
    @Id
    private String userName;
    @Email(message = "Invalid Email address")
    @NotNull(message = "User email is a required field")
    private String userEmail;
    private String profilePicture;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private int gamesDrawn = 0;
    private int rating = 600;
}

