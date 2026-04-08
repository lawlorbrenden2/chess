package websocket;

import chess.ChessGame;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public interface NotificationHandler {
    void notifyMessage(NotificationMessage notificationMessage);
    void notifyError(ErrorMessage errorMessage);
    void loadGame(ChessGame game);
}
