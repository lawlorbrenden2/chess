package server.websocket;


import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import model.data.AuthData;
import model.data.GameData;

import service.GameService;
import websocket.commands.*;
import websocket.messages.*;

import java.time.Duration;

public class WebSocketHandler {

    private final Gson gson = new Gson();
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final AuthDAO authDAO;
    private final GameService gameService;

    public WebSocketHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.authDAO = authDAO;
        this.gameService = new GameService(userDAO, gameDAO, authDAO);
    }

    public void register(Javalin app) {
        app.ws("/ws", ws -> {
            ws.onConnect(ctx -> ctx.session.setIdleTimeout(Duration.ofMinutes(10)));
            ws.onMessage(ctx -> handleMessage(ctx, ctx.message()));
            ws.onClose(connectionManager::removeConnection);
        });
    }

    private void handleMessage(WsContext ctx, String message) {
        try {
            UserGameCommand command =
                    gson.fromJson(message, UserGameCommand.class);

            switch (command.getCommandType()) {
                case CONNECT -> connect(ctx, command);
                case MAKE_MOVE -> {
                    MakeMoveCommand moveCommand = gson.fromJson(message, MakeMoveCommand.class);
                    makeMove(ctx, moveCommand);
                }
                case LEAVE -> {
                    LeaveCommand leaveCommand = gson.fromJson(message, LeaveCommand.class);
                    leave(ctx, leaveCommand);
                }
                case RESIGN -> {
                    ResignCommand resignCommand = gson.fromJson(message, ResignCommand.class);
                    resign(ctx, resignCommand);
                }
                default -> {
                    ctx.send(
                            gson.toJson(new ErrorMessage("Unknown command"))
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connect(WsContext ctx, UserGameCommand command) throws DataAccessException {
        String authToken = command.getAuthToken();
        AuthData authData = authDAO.getAuth(authToken);

        if (authData == null) {
            ctx.send(gson.toJson(new ErrorMessage("Unauthorized")));
            return;
        }

        String username = authData.username();
        connectionManager.addConnection(ctx, username);
        connectionManager.addToGame(ctx, command.getGameID());
        ctx.send(gson.toJson(new NotificationMessage("Connected!")));
    }

    private void makeMove(WsContext ctx, MakeMoveCommand command) {
        try {
            GameData updatedGame = gameService.makeMove(
                    command.getAuthToken(),
                    command.getGameID(),
                    command.getChessMove()
            );

            connectionManager.broadcastToGame(
                    command.getGameID(),
                    gson.toJson(new LoadGameMessage(updatedGame.game()))
            );

        } catch (Exception e) {
            ctx.send(gson.toJson(new ErrorMessage(e.getMessage())));
        }
    }

    private void leave(WsContext ctx, LeaveCommand command) {
        try {
            String authToken = command.getAuthToken();
            AuthData authData = authDAO.getAuth(authToken);

            if (authData == null) {
                ctx.send(gson.toJson(new ErrorMessage("Unauthorized")));
                return;
            }

            String username = authData.username();
            gameService.leave(authToken, command.getGameID());
            connectionManager.removeConnection(ctx);
            connectionManager.broadcastToGameExceptSender(
                    command.getGameID(),
                    ctx,
                    gson.toJson(new NotificationMessage(username + " left the game!"))
                    );

        } catch (Exception e) {
            ctx.send(gson.toJson(new ErrorMessage(e.getMessage())));
        }
    }

    private void resign(WsContext ctx, ResignCommand command) {
        try {
            String authToken = command.getAuthToken();
            AuthData authData = authDAO.getAuth(authToken);

            if (authData == null) {
                ctx.send(gson.toJson(new ErrorMessage("Unauthorized")));
                return;
            }

            String username = authData.username();
            GameData updatedGame = gameService.resign(authToken, command.getGameID());
            connectionManager.removeConnection(ctx);
            connectionManager.broadcastToGameExceptSender(
                    command.getGameID(),
                    ctx,
                    gson.toJson(new NotificationMessage(username + " resigned!"))
            );

            connectionManager.broadcastToGame(
                    command.getGameID(),
                    gson.toJson(new LoadGameMessage(updatedGame.game()))
            );

        } catch (Exception e) {
            ctx.send(gson.toJson(new ErrorMessage(e.getMessage())));
        }
    }

}