package com.example.chess.service;

import com.example.chess.domain.Game;
import com.example.chess.exception.GameNotFoundException;
import com.example.chess.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;
    @Autowired
    public GameService( UserService userService, GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game getGameByGameId(String gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent())
            return game.get();
        throw new GameNotFoundException();
    }
    public List<Game> getAllGames(){
        return gameRepository.findAll();
    }

    public void addGame (Game game) {
        gameRepository.save(game);
    }


    public void updateGame(Game game) {
        gameRepository.save(game);
    }
}
