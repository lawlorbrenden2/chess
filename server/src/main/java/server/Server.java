package server;

import dataaccess.*;
import dataaccess.memorydao.MemoryAuthDAO;
import dataaccess.memorydao.MemoryGameDAO;
import dataaccess.memorydao.MemoryUserDAO;
import io.javalin.*;
import server.handlers.*;
import service.ClearService;
import service.GameService;
import service.UserService;


public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        UserService userService = new UserService(userDAO, authDAO);
        GameService gameService = new GameService(gameDAO, authDAO);
        ClearService clearService = new ClearService(gameDAO, userDAO, authDAO);

        // Register your endpoints and exception handlers here.
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

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
