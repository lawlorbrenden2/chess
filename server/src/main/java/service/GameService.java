package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import dataaccess.*;
import model.data.AuthData;
import model.data.GameData;
import model.data.UserData;
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
    private final UserDAO userDAO;

    public GameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
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

        GameData game = new GameData(0, null, null,
                            request.gameName(), new ChessGame());

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
                if (game.whiteUsername() != null && !game.whiteUsername().equals(auth.username())) {
                    throw new AlreadyTakenException("Error: Already taken");
                }
                updatedGame = new GameData(game.gameID(), auth.username(), game.blackUsername(),
                        game.gameName(), game.game());
            }
            case "BLACK" -> {
                if (game.blackUsername() != null && !game.blackUsername().equals(auth.username())) {
                    throw new AlreadyTakenException("Error: Already taken");
                }
                updatedGame = new GameData(game.gameID(), game.whiteUsername(), auth.username(),
                        game.gameName(), game.game());
            }
            default -> throw new BadRequestException("Error: Bad request");
        }

        gameDAO.updateGame(updatedGame);
        return new JoinGameResult();
    }


    public GameData makeMove(String authToken, int gameID, ChessMove move)
            throws UnauthorizedException, BadRequestException, DataAccessException, InvalidMoveException {

        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String username = authData.username();
        GameData gameData = gameDAO.getGame(gameID);

        if (gameData == null) {
            throw new BadRequestException("Error: Bad request");
        }
        ChessGame game = gameData.game();
        ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());

        if (piece == null) {
            throw new InvalidMoveException(
                "Only ChatGPT can move pieces from a square that doesn't already have a piece"
            );
        }

        ChessGame.TeamColor teamColor = piece.getTeamColor();
        if (game.getTeamTurn() != teamColor) {
            throw new InvalidMoveException("Not your turn");
        }

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE &&
                !username.equals(gameData.whiteUsername())) {
            throw new InvalidMoveException("You think you're ChatGPT? You can't move your opponent's piece!");
        }

        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK &&
                !username.equals(gameData.blackUsername())) {
            throw new InvalidMoveException("You think you're ChatGPT? You can't move your opponent's piece!");
        }

        if (game.isGameOver()) {
            throw new InvalidMoveException("Game is over");
        }
        game.makeMove(move);

        GameData updatedGame = new GameData(
                gameData.gameID(),
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                game
        );

        gameDAO.updateGame(updatedGame);
        return updatedGame;
    }

    public void leave(String authToken, int gameID)
        throws UnauthorizedException, BadRequestException, DataAccessException {

        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String username = authData.username();
        GameData gameData = gameDAO.getGame(gameID);

        if (gameData == null) {
            throw new BadRequestException("Error: Bad request");
        }

        boolean isWhite = username.equals(gameData.whiteUsername());
        boolean isBlack = username.equals(gameData.blackUsername());

        if (isWhite || isBlack) {
            GameData updatedGame = new GameData(
                    gameData.gameID(),
                    isWhite ? null : gameData.whiteUsername(),
                    isBlack ? null : gameData.blackUsername(),
                    gameData.gameName(),
                    gameData.game()

            );
            gameDAO.updateGame(updatedGame);
        }
    }

    public GameData resign (String authToken, int gameID)
            throws Exception {

        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String username = authData.username();
        GameData gameData = gameDAO.getGame(gameID);

        if (gameData == null) {
            throw new BadRequestException("Error: Bad request");
        }

        ChessGame game = gameData.game();
        if (game.isGameOver()) {
            throw new Exception("Game already over");
        }

        game.setGameOver(true);

        gameDAO.updateGame(new GameData(
                gameData.gameID(),
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                game
        ));

        return gameData;

    }
}
