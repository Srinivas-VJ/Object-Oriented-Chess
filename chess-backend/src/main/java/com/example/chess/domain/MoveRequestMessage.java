package com.example.chess.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MoveRequestMessage {
    private String from;
    private String to;
    private String color;
    private String fen;
    private boolean resign;
    @Enumerated(EnumType.STRING)
    private DrawState drawState;

}
