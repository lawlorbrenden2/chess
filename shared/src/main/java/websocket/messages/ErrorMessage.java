package websocket.messages;

public class ErrorMessage extends ServerMessage {
    private String errorMessage;

    public ErrorMessage(ServerMessageType type) {
        super(ServerMessageType.ERROR);
    }

    public String getError() {
        return errorMessage;
    }

    public void setError(String error) {
        this.errorMessage = error;
    }
}
