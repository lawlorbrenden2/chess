package server.websocket;


import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import model.data.GameData;

import service.GameService;
import websocket.commands.*;
import websocket.messages.*;

import java.time.Duration;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class WebSocketHandler {

    private final Gson gson = new Gson();
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final GameService gameService;

    public WebSocketHandler(GameService gameService) {
        this.gameService = gameService;
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
                    ctx.send(gson.toJson(new ErrorMessage("Error: Unknown command")));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connect(WsContext ctx, UserGameCommand command) {
        try {
            String username = gameService.getUsername(command.getAuthToken());
            GameData gameData = gameService.getGameData(command.getAuthToken(), command.getGameID());

            connectionManager.addConnection(ctx, username);
            connectionManager.addToGame(ctx, command.getGameID());

            ctx.send(gson.toJson(new LoadGameMessage(gameData.game())));

            String role = "an observer.";

            if (username.equals(gameData.whiteUsername())) {
                role = "White.";
            } else if (username.equals(gameData.blackUsername())) {
                role = "Black.";
            }

            String joinMessage = username + " joined the game as " + role;
            connectionManager.broadcastToGameExceptSender(
                    command.getGameID(),
                    ctx,
                    gson.toJson(new NotificationMessage(joinMessage))
            );
        } catch (Exception e) {
            ctx.send(gson.toJson(new ErrorMessage("Error: " + e.getMessage())));        }
    }

    private void makeMove(WsContext ctx, MakeMoveCommand command) {
        try {
            String username = gameService.getUsername(command.getAuthToken());
            GameData updatedGame = gameService.makeMove(
                    command.getAuthToken(),
                    command.getGameID(),
                    command.getChessMove()
            );

            String moveNotification = username + " played " + formatMove(command);
            connectionManager.broadcastToGameExceptSender(
                    command.getGameID(),
                    ctx,
                    gson.toJson(new NotificationMessage(moveNotification))
            );

            connectionManager.broadcastToGame(
                    command.getGameID(),
                    gson.toJson(new LoadGameMessage(updatedGame.game()))
            );

            ChessGame gameState = updatedGame.game();
            if (gameState.isGameOver()) {
                if (gameState.isInCheckmate(WHITE)) {
                    String msg = "Checkmate! " +  updatedGame.blackUsername() + " wins!";
                    connectionManager.broadcastToGame(command.getGameID(), gson.toJson(new NotificationMessage(msg)));
                } else if (gameState.isInCheckmate(BLACK)) {
                    String msg = "Checkmate! " +  updatedGame.whiteUsername() + " wins!";
                    connectionManager.broadcastToGame(command.getGameID(), gson.toJson(new NotificationMessage(msg)));
                }
                else {
                    String msg = "Stalemate! The game ends in a draw!";
                    connectionManager.broadcastToGame(command.getGameID(), gson.toJson(new NotificationMessage(msg)));
                }
            }
            if (gameState.isInCheck(WHITE) && !gameState.isGameOver()) {
                String msg = updatedGame.whiteUsername() + " is in Check!";
                connectionManager.broadcastToGame(command.getGameID(), gson.toJson(new NotificationMessage(msg)));
            } else if (gameState.isInCheck(BLACK) && !gameState.isGameOver()) {
                String msg = updatedGame.blackUsername() + " is in Check!";
                connectionManager.broadcastToGame(command.getGameID(), gson.toJson(new NotificationMessage(msg)));
            }

        } catch (Exception e) {
            ctx.send(gson.toJson(new ErrorMessage("Error: " + e.getMessage())));        }
    }

    private void leave(WsContext ctx, LeaveCommand command) {
        try {
            String authToken = command.getAuthToken();
            String username = gameService.getUsername(command.getAuthToken());
            if (authToken == null) {
                ctx.send(gson.toJson(new ErrorMessage("Unauthorized")));
                return;
            }

            gameService.leave(authToken, command.getGameID());
            connectionManager.removeConnection(ctx);
            connectionManager.broadcastToGameExceptSender(
                    command.getGameID(),
                    ctx,
                    gson.toJson(new NotificationMessage(username + " left the game."))
                    );

        } catch (Exception e) {
            ctx.send(gson.toJson(new ErrorMessage("Error: " + e.getMessage())));        }
    }

    private void resign(WsContext ctx, ResignCommand command) {
        try {
            String authToken = command.getAuthToken();
            String username = gameService.getUsername(command.getAuthToken());

            if (authToken == null) {
                ctx.send(gson.toJson(new ErrorMessage("Unauthorized")));
                return;
            }

            GameData updatedGame = gameService.resign(authToken, command.getGameID());
            String winner = username.equals(updatedGame.whiteUsername()) ?
                    updatedGame.blackUsername() : updatedGame.whiteUsername();
            String resignMessage = username + " resigned. " + winner + " wins!";

            connectionManager.broadcastToGame(
                    command.getGameID(),
                    gson.toJson(new NotificationMessage(resignMessage))
            );

            connectionManager.broadcastToGame(
                    command.getGameID(),
                    gson.toJson(new LoadGameMessage(updatedGame.game()))
            );

        } catch (Exception e) {
            ctx.send(gson.toJson(new ErrorMessage("Error: " + e.getMessage())));        }
    }

    private String formatMove(MakeMoveCommand command) {
        ChessPosition startPos = command.getChessMove().getStartPosition();
        ChessPosition endPos = command.getChessMove().getEndPosition();

        char startCol = (char) ('a' + startPos.getColumn() - 1);
        char endCol = (char) ('a' + endPos.getColumn() - 1);

        return "" + startCol + startPos.getRow() + endCol + endPos.getRow();
    }
}