package websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public interface NotificationHandler {
    void notify(NotificationMessage notificationMessage);
    void error(ErrorMessage errorMessage);
    void loadGame(LoadGameMessage gameMessage);
}
