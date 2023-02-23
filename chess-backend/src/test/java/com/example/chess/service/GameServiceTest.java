package com.example.chess.service;

import com.example.chess.domain.Game;
import com.example.chess.exception.GameNotFoundException;
import com.example.chess.repository.GameRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    static Game dummyGame;

    @BeforeAll
    static void beforeAll() {
        dummyGame = new Game("1", "test", "test", "white", new ArrayList<>(), "", "");
    }


    @Test
    void getGameByGameId() {
        when(gameRepository.findById("1")).thenReturn(Optional.ofNullable(dummyGame));
        Optional<Game> game = Optional.ofNullable(gameService.getGameByGameId("1"));
        assertTrue(game.isPresent());
    }
    @Test
    void getNonExistentGameByGameId() {
        assertThrows(GameNotFoundException.class, () -> gameService.getGameByGameId("2"));
    }

    @Test
    void getAllGames() {
        gameService.getAllGames();
        verify(gameRepository).findAll();
    }

    @Test
    void addGame() {
        gameService.addGame(dummyGame);
        ArgumentCaptor<Game> gameArgumentCaptor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).save(gameArgumentCaptor.capture());
        Game capturedGame = gameArgumentCaptor.getValue();
        assertEquals(capturedGame, dummyGame);
    }

}