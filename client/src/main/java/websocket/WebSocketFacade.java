package websocket;

import com.google.gson.Gson;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.awt.*;
import java.io.IOException;
import java.net.URI;



public class WebSocketFacade extends Endpoint {
    private Session session;
    private final NotificationHandler handler;
    private final Gson gson = new Gson();

    public WebSocketFacade(NotificationHandler handler) {
        this.handler = handler;
    }

    public void connect(String url) throws Exception {
        URI uri = new URI(url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, uri);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                handleMessage(message);
            }
        });
    }

    public void sendCommand(UserGameCommand command) {
        try {
            session.getBasicRemote().sendText(gson.toJson(command));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleMessage(String message) {
        try {
            ServerMessage baseMessage = gson.fromJson(message, ServerMessage.class);
            switch (baseMessage.getServerMessageType()) {
                case ERROR -> {
                    ErrorMessage errorMessage = gson.fromJson(message, ErrorMessage.class);
                    handler.notifyError(errorMessage);
                }
                case LOAD_GAME -> {
                    LoadGameMessage loadGameMessage = gson.fromJson(message, LoadGameMessage.class);
                    handler.loadGame(loadGameMessage.getGame());
                }
                case NOTIFICATION -> {
                    NotificationMessage notificationMessage = gson.fromJson(message, NotificationMessage.class);
                    handler.notifyMessage(notificationMessage);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
