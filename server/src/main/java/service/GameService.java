package service;

import model.data.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.ListGamesRequest;
import model.result.CreateGameResult;
import model.result.JoinGameResult;
import model.result.ListGamesResult;

import java.util.ArrayList;

public class GameService {
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
