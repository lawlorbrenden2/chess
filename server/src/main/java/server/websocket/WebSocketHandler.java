package server.websocket;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

public class WebSocketHandler {

    private final Gson gson = new Gson();

    public void register(Javalin app) {

        app.ws("/ws", ws -> {
            ws.onMessage(ctx -> handleMessage(ctx, ctx.message()));
        });
    }

    private void handleMessage(WsContext ctx, String message) {
        try {
            UserGameCommand command =
                    gson.fromJson(message, UserGameCommand.class);

            switch (command.getCommandType()) {
                case CONNECT -> {
                    ctx.send(
                            gson.toJson(new NotificationMessage("Connected!"))
                    );
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