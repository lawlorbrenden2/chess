package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.commands.*;
import com.google.gson.Gson;
import io.javalin.Javalin;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

public class WebSocketHandler {

    Gson gson = new Gson();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = gson.fromJson(message,UserGameCommand.class);
                switch (command.getCommandType()) {
                    case CONNECT -> {
                        session.getRemote().sendString(
                                gson.toJson(new NotificationMessage("Connected!"))
                        );
                    }
                    default -> {
                        session.getRemote().sendString(
                                gson.toJson(new ErrorMessage("Unknown command"))
                        );
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
