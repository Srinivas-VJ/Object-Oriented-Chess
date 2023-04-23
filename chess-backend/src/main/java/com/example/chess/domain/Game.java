package com.example.chess.domain;

import com.example.chess.Board.Board;
import com.example.chess.pieces.Colour;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
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
    @JsonIgnore
    @Transient
    @Enumerated
    private Colour currentPlayerColor = Colour.WHITE;
    private List<String> moves;
    private String status;
    private String description;
    @Transient
    @JsonIgnore
    private Board board;
    @Transient
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private DrawState drawState;

    public void makeMove(String move) {
        moves.add(move);
    }
}

