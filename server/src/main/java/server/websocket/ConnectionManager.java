package server.websocket;

import model.data.GameData;
import io.javalin.websocket.WsContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.*;

public class ConnectionManager {

    private final Map<WsContext, String> sessionToUser = new HashMap<>();
    private final Map<String, WsContext> userToSession = new HashMap<>();
    private final Map<Integer, Set<WsContext>> gameToSessions = new HashMap<>();
    private final Map<WsContext, Integer> sessionToGame = new HashMap<>();

    public void addConnection(WsContext ctx, String username) {
        sessionToUser.put(ctx, username);
        userToSession.put(username, ctx);
    }

    public void removeConnection(WsContext ctx) {
        sessionToUser.remove(ctx);

        Integer gameID = sessionToGame.remove(ctx);
        if (gameID != null) {
            Set<WsContext> sessions = gameToSessions.get(gameID);
            if (sessions != null) {
                sessions.remove(ctx);
            }
        }
    }

    public void addToGame(WsContext ctx, Integer gameID) {
        sessionToGame.put(ctx, gameID);
        gameToSessions.computeIfAbsent(gameID, k -> new HashSet<>()).add(ctx);
    }

    public Set<WsContext> getGameSessions(Integer gameID) {
        return gameToSessions.getOrDefault(gameID, Set.of());
    }

    public void sendToSession(WsContext ctx, String message) {
        ctx.send(message);
    }

    public void broadcastToGame(int gameId, String message) {
        for (WsContext ctx : getGameSessions(gameId)) {
            sendToSession(ctx, message);
        }
    }
}
