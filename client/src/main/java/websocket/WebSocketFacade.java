package websocket;

import websocket.messages.NotificationMessage;

public interface WebSocketFacade {
    void notify(NotificationMessage notificationMessage);
}
