package server.websocket;

import model.data.GameData;
import jakarta.websocket.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConnectionManager {

    private final Map<Session, String> sessionToUser = new HashMap<>();
    private final Map<String, Session> userToSession = new HashMap<>();
    private final Map<Integer, Set<Session>> gameToSessions = new HashMap<>();
    private final Map<Session, Integer> sessionToGame = new HashMap<>();

    public void addConnection(Session session, String username) {
        sessionToUser.put(session, username);
    }

    public void removeConnection(Session session) {
        sessionToUser.remove(session);

        Integer gameID = sessionToGame.remove(session);
        if (gameID != null) {
            Set<Session> sessions = gameToSessions.get(gameID);
            if (sessions != null) {
                sessions.remove(session);
            }
        }
    }

    public void addToGame(Session session, Integer gameID) {
        sessionToGame.put(session, gameID);
        gameToSessions.computeIfAbsent(gameID, k -> new HashSet<>()).add(session);
    }

    public Set<Session> getGameSessions(Integer gameID) {
        return gameToSessions.getOrDefault(gameID, Set.of());
    }


}
