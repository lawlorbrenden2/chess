package service;

import dataaccess.*;
import model.data.AuthData;
import model.data.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.ListGamesRequest;
import model.result.CreateGameResult;
import model.result.JoinGameResult;
import model.result.ListGamesResult;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;

import java.util.ArrayList;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResult listGames(ListGamesRequest request) {
        return new ListGamesResult(new ArrayList<>());
    }


    public CreateGameResult createGame(CreateGameRequest request)
            throws BadRequestException, UnauthorizedException, DataAccessException {

        if (request.gameName() == null) {
            throw new BadRequestException("Error: Bad request");
        }

        AuthData auth = authDAO.getAuth(request.authToken());

        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        GameData game = new GameData(0, null, null, request.gameName());

        int gameID = gameDAO.createGame(game);
        return new CreateGameResult(gameID);
    }


    public JoinGameResult joinGame(JoinGameRequest request) {
        return new JoinGameResult();
    }

}
