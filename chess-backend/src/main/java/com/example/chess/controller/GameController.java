package com.example.chess.controller;

import com.example.chess.domain.*;
import com.example.chess.exception.GameHasNoPlayerException;
import com.example.chess.exception.GameNotFoundException;
import com.example.chess.exception.InvalidMoveException;
import com.example.chess.exception.UserNotFoundException;
import com.example.chess.pieces.Board;
import com.example.chess.pieces.Colour;
import com.example.chess.service.GameService;
import com.example.chess.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
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

    private final Map<String, Game> ongoingGames = new HashMap<>();
    private final Map<String, String> gameStates = new HashMap<>();

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
        if (gameStates.containsKey(gameId) && gameStates.get(gameId).length() != 0)
           return ResponseEntity.ok(gameStates.get(gameId));
        return ResponseEntity.ok("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }
    @PutMapping("/game/{gameId}/moved")
    public ResponseEntity<Object> updateLastMove(@PathVariable String gameId, @RequestBody MoveRequestMessage  move, @AuthenticationPrincipal User authenticatedUser) {
        Game game = gameService.getGameByGameId(gameId);
        String player = move.getFen().split(" ")[1];
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
    public ResponseEntity<String> createGame(@RequestBody Game game, Principal principal) {
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

        String opponent = Objects.equals(principal.getName(), game.getPlayerBlack()) ? game.getPlayerWhite() : game.getPlayerBlack();
        template.convertAndSendToUser(opponent , "/notification", game);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{gameID}").buildAndExpand(game.getGameID()).toUri();
        return new ResponseEntity<>(game.getGameID(), HttpStatus.CREATED);
    }
    @PutMapping("/move/{gameId}")
    public ResponseEntity<Object> processMoveRequest(@PathVariable String gameId, @RequestBody MoveRequestMessage moveRequestMessage, Principal principal) throws InvalidMoveException {
        // process the move here
        Game game = ongoingGames.get(gameId);
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        String playerChar = moveRequestMessage.getFen().split(" ")[1];

        if (!ongoingGames.containsKey(gameId))
            throw new GameNotFoundException();

        // check resignation
        if (moveRequestMessage.isResign()) {
            template.convertAndSend("/topic/move/" + gameId,  new MoveResponseMessage(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), moveRequestMessage.getColor(), moveRequestMessage.getFen(), "completed",moveRequestMessage.getColor() + " resigned", moveRequestMessage.getDrawState()));
            handleGameOver(game, 3, playerChar.equals("b") ? Colour.BLACK : Colour.WHITE);
            gameStates.remove(gameId);
            return ResponseEntity.ok("Success!");
        }
        // check draw accepted
        if (DrawState.ACCEPTED.equals(moveRequestMessage.getDrawState())) {
            template.convertAndSend("/topic/move/" + gameId, new MoveResponseMessage(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), moveRequestMessage.getColor(), moveRequestMessage.getFen(), "completed", "Game drawn by agreement", moveRequestMessage.getDrawState()));
            handleGameOver(game, 4, playerChar.equals("b") ? Colour.BLACK : Colour.WHITE);
            gameStates.remove(gameId);
            return ResponseEntity.ok("Success!");
        }
        // check draw offered
        if (DrawState.OFFERED.equals(moveRequestMessage.getDrawState())) {
            template.convertAndSend("/topic/move/" + gameId ,new MoveResponseMessage(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), moveRequestMessage.getColor(), moveRequestMessage.getFen(), "active", "ongoing game", moveRequestMessage.getDrawState()));
            return ResponseEntity.ok("Success!");
        }

        if ((playerChar.equals("b") && authenticatedUser.getName().equals(game.getPlayerBlack())) ||
           (playerChar.equals("w") && authenticatedUser.getName().equals(game.getPlayerWhite())))
        {
            game.makeMove(moveRequestMessage.getFrom() + moveRequestMessage.getTo());
            Board board = new Board(moveRequestMessage.getFen());
            Colour player = moveRequestMessage.getColor().equals("white") ? Colour.WHITE : Colour.BLACK;
            int status = board.getMoveStatus(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), player, moveRequestMessage.getFen());
            switch (status) {
                case 0 -> {
                    template.convertAndSend("/topic/move/" + gameId, new MoveResponseMessage(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), moveRequestMessage.getColor(), moveRequestMessage.getFen(), "active", "ongoing game", moveRequestMessage.getDrawState()));
                    gameStates.put(gameId, moveRequestMessage.getFen());
                    return ResponseEntity.ok("Success!");
                }
                case 1 ->
                {
                    template.convertAndSend("/topic/move/" + gameId,  new MoveResponseMessage(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), moveRequestMessage.getColor(), moveRequestMessage.getFen(), "completed",player + " wins by Checkmate", moveRequestMessage.getDrawState()));
                    handleGameOver(game, 1, player);
                    gameStates.remove(gameId);
                    return ResponseEntity.ok("Success!");
                }
                case 2 ->
                {
                    template.convertAndSend("/topic/move/" + gameId, new MoveResponseMessage(moveRequestMessage.getFrom(), moveRequestMessage.getTo(), moveRequestMessage.getColor(), moveRequestMessage.getFen(), "completed", "Game drawn by Stalemate", moveRequestMessage.getDrawState()));
                    handleGameOver(game, 2, player);
                    gameStates.remove(gameId);
                    return ResponseEntity.ok("Success!");
                }
                default -> throw new InvalidMoveException();
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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
            updateRatings(winner, loser);
        }
        else if (status == 2) {
            game.setStatus("Game drawn by Stalemate");
            whitePlayer.setGamesDrawn(whitePlayer.getGamesDrawn() + 1);
            blackPlayer.setGamesDrawn(blackPlayer.getGamesDrawn() + 1);
        }
        else if (status == 3) {
            game.setStatus(player + " resigned");
            User winner = player == Colour.WHITE ? blackPlayer : whitePlayer;
            User loser = player == Colour.BLACK ? blackPlayer : whitePlayer;
            winner.setGamesWon(winner.getGamesWon() + 1);
            loser.setGamesLost(loser.getGamesLost() + 1);
            updateRatings(winner, loser);
        }
        else if (status == 4) {
            game.setStatus("Game drawn by agreement");
            whitePlayer.setGamesDrawn(whitePlayer.getGamesDrawn() + 1);
            blackPlayer.setGamesDrawn(blackPlayer.getGamesDrawn() + 1);
        }
        userService.updateUser(whitePlayer);
        userService.updateUser(blackPlayer);
        gameService.updateGame(game);
    }

    private void updateRatings(User winner, User loser) {
        int factor = 30;
        int winnerRating = winner.getRating().get(winner.getRating().size() - 1);
        int loserRating = loser.getRating().get(loser.getRating().size() - 1);
        float winningProbabilityOfWinner = calculateWinningProbability((float) loserRating, (float) winnerRating);
        float winningProbabilityOfLoser = calculateWinningProbability((float) winnerRating, (float) loserRating);
        winnerRating = Math.round(winnerRating + factor * (1 - winningProbabilityOfWinner));
        loserRating = Math.round(loserRating + factor * (0 - winningProbabilityOfLoser));
        winner.getRating().add(winnerRating);
        loser.getRating().add(loserRating);
    }

    private float calculateWinningProbability(float loserRating, float winnerRating) {
        return 1.0f
                / (1 + (float) (Math.pow(10, (loserRating - winnerRating) / 400)));
    }
}