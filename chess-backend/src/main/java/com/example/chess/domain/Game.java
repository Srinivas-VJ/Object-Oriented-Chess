package com.example.chess.domain;

import jakarta.validation.constraints.NotBlank;
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
    private Integer gameID;
    @NotBlank
    private User playerWhite;

    @NotBlank
    private User playerBlack;
    private List<String> moves;
    private String status;
    private String description;

    private void makeMove(String move) {
        moves.add(move);
    }


}
