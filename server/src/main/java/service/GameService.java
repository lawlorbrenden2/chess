package service;

import dataaccess.*;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.ListGamesRequest;
import model.result.CreateGameResult;
import model.result.JoinGameResult;
import model.result.ListGamesResult;

import java.util.ArrayList;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(MemoryGameDAO gameDAO, MemoryAuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) {
        return new ListGamesResult(new ArrayList<>());
    }
    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        return new CreateGameResult(123);
    }
    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) {
        return new JoinGameResult();
    }

}
