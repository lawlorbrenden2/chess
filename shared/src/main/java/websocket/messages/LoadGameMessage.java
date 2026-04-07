package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    private String game;

    public LoadGameMessage(ServerMessageType type) {
        super(ServerMessageType.LOAD_GAME);
    }
    public String getGame() {
        return game;
    }

    public void setGame() {
        this.game = game;
    }
}
