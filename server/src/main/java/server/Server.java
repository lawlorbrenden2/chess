package server;

import dataaccess.*;
import dataaccess.memorydao.*;
import dataaccess.sqldao.SQLAuthDAO;
import dataaccess.sqldao.SQLGameDAO;
import dataaccess.sqldao.SQLUserDAO;
import io.javalin.*;
import jakarta.websocket.WebSocketContainer;
import server.handlers.*;
import server.websocket.WebSocketHandler;
import service.ClearService;
import service.GameService;
import service.UserService;


public class Server {

    private final Javalin javalin;

    public Server() {
        try {
            javalin = Javalin.create(config -> config.staticFiles.add("web"));
            UserDAO userDAO = new SQLUserDAO();
            AuthDAO authDAO = new SQLAuthDAO();
            GameDAO gameDAO = new SQLGameDAO();

            UserService userService = new UserService(userDAO, authDAO);
            GameService gameService = new GameService(userDAO, gameDAO, authDAO);
            ClearService clearService = new ClearService(gameDAO, userDAO, authDAO);

            WebSocketHandler webSocketHandler = new WebSocketHandler(gameService);
            webSocketHandler.register(javalin);


            RegisterHandler registerHandler = new RegisterHandler(userService);
            javalin.post("/user", registerHandler);

            LoginHandler loginHandler = new LoginHandler(userService);
            javalin.post("/session", loginHandler);

            LogoutHandler logoutHandler = new LogoutHandler(userService);
            javalin.delete("/session", logoutHandler);

            CreateGameHandler createGameHandler = new CreateGameHandler(gameService);
            javalin.post("/game", createGameHandler);

            JoinGameHandler joinGameHandler = new JoinGameHandler(gameService);
            javalin.put("/game", joinGameHandler);

            ListGamesHandler listGamesHandler = new ListGamesHandler(gameService);
            javalin.get("/game", listGamesHandler);

            ClearHandler clearHandler = new ClearHandler(clearService);
            javalin.delete("/db", clearHandler);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize SQL DAOs: " + e.getMessage(), e);
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

}
