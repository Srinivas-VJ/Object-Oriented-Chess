package com.example.chess.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private String gameID;
    private String playerWhite;

    private String playerBlack;
    // might need a different representation for color
    private String currentPlayerColor = "white";
    private List<String> moves;
    private String status;
    private String description;

    public void makeMove(String move) {
        moves.add(move);
    }


}
