package com.example.chess.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MoveResponseMessage {
    private String from;
    private String to;
    private String color;
    private String status;
    private String message;
    @Enumerated(EnumType.STRING)
    private DrawState drawState;
}
