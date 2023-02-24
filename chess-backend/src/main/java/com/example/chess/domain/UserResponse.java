package com.example.chess.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
//@RequiredArgsConstructor
public class UserResponse {
    public UserResponse(User user) {
        this.username = user.getUsername();
        this.rating = user.getRating();
        this.gamesLost = user.getGamesLost();
        this.gamesWon = user.getGamesWon();
        this.gamesDrawn = user.getGamesDrawn();
        this.profilePicture = user.getProfilePicture();
    }
    private String username;
    private String profilePicture;
    private int gamesWon;
    private int gamesLost;

    private int gamesDrawn;

    private List<Integer> rating;
}
