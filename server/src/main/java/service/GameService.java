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
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;


public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResult listGames(ListGamesRequest request)
            throws UnauthorizedException, DataAccessException {

        AuthData auth = authDAO.getAuth(request.authToken());
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return new ListGamesResult(gameDAO.listGames());
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


    public JoinGameResult joinGame(JoinGameRequest request)
            throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {

        if (request.gameID() == null || request.playerColor() == null || request.authToken() == null) {
            throw new BadRequestException("Error: Bad request");
        }

        GameData game = gameDAO.getGame(request.gameID());
        if (game == null) {
            throw new BadRequestException("Error: Bad request");
        }

        AuthData auth = authDAO.getAuth(request.authToken());
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        GameData updatedGame;

        switch(request.playerColor().toUpperCase()) {
            case "WHITE" -> {
                if (game.whiteUsername() != null) {
                    throw new AlreadyTakenException("Error: Already taken");
                }
                updatedGame = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName());
            }
            case "BLACK" -> {
                if (game.blackUsername() != null) {
                    throw new AlreadyTakenException("Error: Already taken");
                }
                updatedGame = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName());
            }
            default -> throw new BadRequestException("Error: Bad request");
        }

        gameDAO.updateGame(updatedGame);

        return new JoinGameResult();
    }


}
