package com.example.chess.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MoveResponseMessage {
    private String from;
    private String to;
}
