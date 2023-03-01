package com.example.chess.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Game {
    @Id
    private String gameID;
    private String playerWhite;
    private String playerBlack;
    private String currentPlayerColor = "white";
    private List<String> moves;
    private String status;
    private String description;

    @Enumerated(EnumType.STRING)
    private DrawState drawState;

    public void makeMove(String move) {
        moves.add(move);
    }


}

