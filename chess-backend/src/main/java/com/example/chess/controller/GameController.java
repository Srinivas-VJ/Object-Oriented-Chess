package com.example.chess.controller;

import com.example.chess.domain.MoveRequestMessage;
import com.example.chess.domain.MoveResponseMessage;
import com.example.chess.exception.GameHasNoPlayerException;
import com.example.chess.exception.InvalidMoveException;
import com.example.chess.pieces.Board;
import com.example.chess.pieces.Colour;
import com.example.chess.service.GameService;
import com.example.chess.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Timestamp;
import java.util.*;

@RestController()
@CrossOrigin(origins = "http://localhost:3000")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/game")
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }
    @GetMapping("/game/{gameId}")
    public Game getGameByGameId(@PathVariable String gameId) {
        return gameService.getGameByGameId(gameId);
    }

    @PostMapping("/game")
    public ResponseEntity<String > createGame(@RequestBody Game game) {
        // generate gameID
        // check  if  both playerwhite  and playerblack  exist  in the database
        if (game.getPlayerBlack() ==  null  && game.getPlayerWhite() == null)
            throw new GameHasNoPlayerException();
        String message = "";
        if (game.getPlayerWhite() != null)
            message += game.getPlayerWhite();
        if (game.getPlayerBlack() != null)
            message  += game.getPlayerBlack();
        message +=  new Date().getTime();
        game.setGameID(UUID.nameUUIDFromBytes(message.getBytes()).toString());
        gameService.addGame(game);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{gameID}").buildAndExpand(game.getGameID()).toUri();
        return new ResponseEntity<>(game.getGameID(), HttpStatus.CREATED);
    }
    @MessageMapping("/move/{gameId}")
    @SendTo("/topic/move/{gameId}")
    public MoveResponseMessage processMoveRequest(@DestinationVariable String gameId, MoveRequestMessage moveRequestMessage) throws Exception{
        // process the move here
        Game game = gameService.getGameByGameId(gameId);
        game.makeMove(moveRequestMessage.getFrom() + moveRequestMessage.getTo());
        Board board = new Board(moveRequestMessage.getFen());
        Colour player = moveRequestMessage.getColor().equals("white") ? Colour.WHITE : Colour.BLACK;
        if (board.isMoveMakeable(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), player))
            return new MoveResponseMessage(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), moveRequestMessage.getColor(), moveRequestMessage.getFen());
        throw new InvalidMoveException();
    }
}

