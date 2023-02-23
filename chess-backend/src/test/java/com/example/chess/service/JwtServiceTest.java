package com.example.chess.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;


class JwtServiceTest {
    @Autowired
    private JwtService jwtService = new JwtService();

    @Test
    @Disabled
    // disabled cause token keeps expiring
    void extractUserName() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTY3NzEzODkyMSwiZXhwIjoxNjc3MTgyMTIxfQ.CBBBR39_my5oyxG5lEiS3objHw8dvw90J-UmdF4Yn6Q";
        String actual = jwtService.extractUserName(token);
        assertEquals("test", actual);
    }

}