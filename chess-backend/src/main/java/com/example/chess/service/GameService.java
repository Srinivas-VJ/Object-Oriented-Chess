package com.example.chess.service;

import com.example.chess.domain.Game;
import com.example.chess.exception.GameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class GameService {

    private UserService userService;
    @Autowired
    public GameService( UserService userService) {
        this.userService = userService;
    }
    private static List<Game> games = new ArrayList<Game>();
    static {
            games.add(new Game("1", "srini", "ratna", "white", new ArrayList<>(), "intialted", "fresh game"));
            games.add(new Game("2", "rahul", "ratna", "white", new ArrayList<>(), "intialted", "fresh game"));
            games.add(new Game("3", "rahul", "srini", "white", new ArrayList<>(), "intialted", "fresh game"));
            games.add(new Game("4", "srini", "rahul", "white", new ArrayList<>(), "intialted", "fresh game"));
    }

    public Game getGameByGameId(String gameId) {
        for (Game game : games)
            if (game.getGameID().equals(gameId))
                return  game;
        throw new GameNotFoundException();
    }
    public List<Game> getAllGames(){
        return games;
    }

    public void addGame (Game game) {
        games.add(game);
    }



}
