package websocket.messages;


public class NotificationMessage extends ServerMessage {
    private String message;

    public NotificationMessage(ServerMessageType type) {
        super(ServerMessageType.NOTIFICATION);
    }

    public String getNotification() {
        return message;
    }

    public void setNotification(String game) {
        this.message = message;
    }
}
