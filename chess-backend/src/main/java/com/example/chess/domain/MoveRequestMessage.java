package com.example.chess.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MoveRequestMessage {
    private String from;
    private String to;

    // may need a different representation for color;
    private String color;

    private String fen;

}
