package com.example.chess.controller;

import com.example.chess.domain.MoveRequestMessage;
import com.example.chess.domain.MoveResponseMessage;
import com.example.chess.service.GameService;
import com.example.chess.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Object> createGame(@RequestBody Game game) {
        gameService.addGame(game);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{gameID}").buildAndExpand(game.getGameID()).toUri();
        return ResponseEntity.created(location).build();
    }
    @MessageMapping("/move/{gameId}")
    @SendTo("/topic/move/{gameId}")
    public MoveResponseMessage processMoveRequest(@DestinationVariable String gameId, MoveRequestMessage moveRequestMessage) throws Exception{
        // process the move here
        System.out.println("Game ID -> " + gameId);
        Game game = gameService.getGameByGameId(Integer.parseInt(gameId));
        game.makeMove(moveRequestMessage.getFrom() + moveRequestMessage.getTo());
        return new MoveResponseMessage(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), moveRequestMessage.getColor());
    }
}

