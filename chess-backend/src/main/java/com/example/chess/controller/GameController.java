package com.example.chess.controller;

import com.example.chess.domain.Game;
import com.example.chess.domain.MoveRequestMessage;
import com.example.chess.domain.MoveResponseMessage;
import com.example.chess.domain.User;
import com.example.chess.exception.GameHasNoPlayerException;
import com.example.chess.exception.GameNotFoundException;
import com.example.chess.exception.InvalidMoveException;
import com.example.chess.exception.UserNotFoundException;
import com.example.chess.pieces.Board;
import com.example.chess.pieces.Colour;
import com.example.chess.service.GameService;
import com.example.chess.service.JwtService;
import com.example.chess.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController()
@CrossOrigin(origins = "${frontend.url}")
public class GameController {

    @Autowired
    private GameService gameService;
    @Autowired
    private UserService userService;
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private JwtService jwtService;

    private Map<String, Game> ongoingGames = new HashMap<>();
    private Map<String, String> gameStates = new HashMap<>();

    @GetMapping("/game")
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }
    @GetMapping("/game/{gameId}")
    public Game getGameByGameId(@PathVariable String gameId) {
        return gameService.getGameByGameId(gameId);
    }
    @GetMapping("/game/{gameId}/getFen")
    public ResponseEntity<String> getGameState(@PathVariable String gameId) {
        if (gameStates.containsKey(gameId)) {
           return ResponseEntity.ok(gameStates.get(gameId));
        }
        return ResponseEntity.ok("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    @PutMapping("/game/{gameId}/moved")
    public ResponseEntity<Object> updateLastMove(@PathVariable String gameId, @RequestBody MoveRequestMessage  move, @AuthenticationPrincipal User authenticatedUser) {
//        Game game = gameService.getGameByGameId(gameId);
//        String player = move.getFen().split(" ")[1];
//        if ((player.equals("w") && authenticatedUser.getUsername().equals(game.getPlayerBlack())) ||
//           (player.equals("b") && authenticatedUser.getUsername().equals(game.getPlayerWhite())))
//        {
            if (gameStates.containsKey(gameId)) {
                gameStates.put(gameId, move.getFen());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }
    @PostMapping("/game")
    public ResponseEntity<String> createGame(@RequestBody Game game) {
        // generate gameID
        // check  if  both player-white  and player-black  exist  in the database
        if (game.getPlayerBlack() ==  null || game.getPlayerWhite() == null)
            throw new GameHasNoPlayerException();

        var user1  = userService.getUserByUserName(game.getPlayerBlack());
        var user2  = userService.getUserByUserName(game.getPlayerWhite());
        if (user1 == null || user2  == null)
            throw new UserNotFoundException();

        String message = game.getPlayerBlack() + game.getPlayerWhite();
        message +=  new Date().getTime();
        game.setGameID(UUID.nameUUIDFromBytes(message.getBytes()).toString());

        gameService.addGame(game);
        ongoingGames.put(game.getGameID(), game);
        gameStates.put(game.getGameID(), "");

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{gameID}").buildAndExpand(game.getGameID()).toUri();
        return new ResponseEntity<>(game.getGameID(), HttpStatus.CREATED);
    }

    @PutMapping("/move/{gameId}")
    public ResponseEntity<Object> processMoveRequest(@PathVariable String gameId, @RequestBody MoveRequestMessage moveRequestMessage, @AuthenticationPrincipal User authenticatedUser) throws InvalidMoveException {
        // process the move here
        Game game = ongoingGames.get(gameId);
//        String playerChar = moveRequestMessage.getFen().split(" ")[1];
//        if ((playerChar.equals("w") && authenticatedUser.getUsername().equals(game.getPlayerBlack())) ||
//           (playerChar.equals("b") && authenticatedUser.getUsername().equals(game.getPlayerWhite())))
//        {
            if (!ongoingGames.containsKey(gameId))
                throw new GameNotFoundException();


            game.makeMove(moveRequestMessage.getFrom() + moveRequestMessage.getTo());
            Board board = new Board(moveRequestMessage.getFen());
            Colour player = moveRequestMessage.getColor().equals("white") ? Colour.WHITE : Colour.BLACK;
            int status = board.getMoveStatus(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), player, moveRequestMessage.getFen());
            switch (status) {
                case 0 -> {
                    gameStates.put(gameId, moveRequestMessage.getFen());
                    template.convertAndSend("/topic/move/" + gameId, new MoveResponseMessage(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), moveRequestMessage.getColor(), moveRequestMessage.getFen(), "active", "ongoing game"));
                    return ResponseEntity.ok("Success!");
                }
                case 1 ->
                {
                    // end game procedure , try async
                    handleGameOver(game, 1, player);
                    template.convertAndSend("/topic/move/{gameId}",  new MoveResponseMessage(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), moveRequestMessage.getColor(), moveRequestMessage.getFen(), "completed",player + " wins by Checkmate"));
                    gameStates.remove(gameId);
                    return ResponseEntity.ok("Success!");
                }
                case 2 ->
                {
                    // end game procedure
                    handleGameOver(game, 2, player);
                    template.convertAndSend("/topic/move/{gameId}", new MoveResponseMessage(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), moveRequestMessage.getColor(), moveRequestMessage.getFen(), "completed", "Game drawn by Stalemate"));
                    gameStates.remove(gameId);
                    return ResponseEntity.ok("Success!");
                }
                default -> throw new InvalidMoveException();
            }
//        }
//        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    private void handleGameOver(Game game, int status, Colour player) {
        ongoingGames.remove(game.getGameID());
        User whitePlayer = userService.getUserByUserName(game.getPlayerWhite());
        User blackPlayer = userService.getUserByUserName(game.getPlayerBlack());

        game.setDescription(String.format("Game between %s as white and %s as black", whitePlayer.getUsername(), blackPlayer.getUsername()));

        if (status == 1 ) {
            game.setStatus(player + " wins by checkmate");
            User winner = player == Colour.WHITE ? whitePlayer : blackPlayer;
            User loser = player == Colour.BLACK ? whitePlayer : blackPlayer;
            winner.setGamesWon(winner.getGamesWon() + 1);
            loser.setGamesLost(loser.getGamesLost() + 1);
            // rating change logic here
        }
        else {
            game.setStatus("Game drawn by Stalemate");
            whitePlayer.setGamesDrawn(whitePlayer.getGamesDrawn() + 1);
            blackPlayer.setGamesDrawn(blackPlayer.getGamesDrawn() + 1);
        }

        userService.updateUser(whitePlayer);
        userService.updateUser(blackPlayer);
        gameService.updateGame(game);
    }
}

