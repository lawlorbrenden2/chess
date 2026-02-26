package server;

import dataaccess.*;
import io.javalin.*;
import server.handlers.RegisterHandler;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserService userService = new UserService(userDAO, authDAO);

        // Register your endpoints and exception handlers here.
        RegisterHandler registerHandler = new RegisterHandler(userService);
        javalin.post("/user", registerHandler);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
