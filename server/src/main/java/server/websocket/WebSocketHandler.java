package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import model.data.AuthData;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

public class WebSocketHandler {

    private final Gson gson = new Gson();
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final AuthDAO authDAO;

    public WebSocketHandler(UserService userService, AuthDAO authDAO) {
        this.authDAO = authDAO;
    }


    public void register(Javalin app) {
        app.ws("/ws", ws -> {
            ws.onMessage(ctx -> handleMessage(ctx, ctx.message()));
            ws.onClose(connectionManager::removeConnection);
        });
    }

    private void handleMessage(WsContext ctx, String message) {
        try {
            UserGameCommand command =
                    gson.fromJson(message, UserGameCommand.class);

            switch (command.getCommandType()) {
                case CONNECT -> {
                    String authToken = command.getAuthToken();
                    AuthData auth = authDAO.getAuth(authToken);

                    if (auth == null) {
                        ctx.send(gson.toJson(new ErrorMessage("Unauthorized")));
                        return;
                    }

                    String username = auth.username();
                    connectionManager.addConnection(ctx, username);
                    ctx.send(gson.toJson(new NotificationMessage("Connected!")));
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
}