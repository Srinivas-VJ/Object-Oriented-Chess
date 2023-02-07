package com.example.chess.controller;

import com.example.chess.domain.MoveRequestMessage;
import com.example.chess.domain.MoveResponseMessage;
import com.example.chess.service.GameService;
import com.example.chess.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController()
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/game")
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }
    @GetMapping("/game/{gameId}")
    public Game getGameByGameId(@PathVariable Integer gameId) {
        return gameService.getGameByGameId(gameId);
    }

    @PostMapping("/game")
    public ResponseEntity<Object> createGame(Game game) {
        gameService.addGame(game);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{gameId}").buildAndExpand(game.getGameID()).toUri();
        return ResponseEntity.created(location).build();
    }
    @MessageMapping("/move/{gameId}")
    @SendTo("/topic/move/{gameId}")
    public MoveResponseMessage processMoveRequest(@PathVariable String gameId, MoveRequestMessage moveRequestMessage) throws Exception{
        // process the move here
        System.out.println("Game ID -> " + gameId);
        return new MoveResponseMessage(moveRequestMessage.getFrom(), moveRequestMessage.getTo());
    }
}

