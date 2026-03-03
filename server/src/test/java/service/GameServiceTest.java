package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTest {
    private GameService gameService;
    private UserService userService;

    @BeforeEach
    void setUp() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);

        RegisterRequest registerRequest = new RegisterRequest("user123", "pass67", "my_email@byu.edu");
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("user123", "pass67");
        LoginResult loginResult = userService.login(loginRequest);
        String authToken = loginResult.authToken();
    }

    @Test
    void listGamesPositive() throws Exception {

    }

    @Test
    void listGamesNegative() throws Exception {

    }

    @Test
    void createGamePositive() throws Exception {

    }

    @Test
    void createGameNegative() throws Exception {

    }

    @Test
    void joinGamePositive() throws Exception {

    }

    @Test
    void joinGameNegative() throws Exception {

    }
}
