package service;

import dataaccess.*;
import model.data.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.CreateGameResult;
import model.result.JoinGameResult;
import model.result.LoginResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exceptions.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private GameService gameService;
    private UserService userService;
    private String authToken;
    private GameDAO gameDAO;

    @BeforeEach
    void setUp() throws Exception {
        UserDAO userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);

        RegisterRequest registerRequest = new RegisterRequest("user123", "pass67", "my_email@byu.edu");
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("user123", "pass67");
        LoginResult loginResult = userService.login(loginRequest);
        authToken = loginResult.authToken();
    }

    private CreateGameResult createGame(String authToken, String gameName) throws Exception {
        CreateGameRequest request = new CreateGameRequest(authToken, gameName);
        return gameService.createGame(request);
    }

    @Test
    void listGamesPositive() throws Exception {

    }

    @Test
    void listGamesNegative() throws Exception {

    }

    @Test
    void createGamePositive() throws Exception {
        CreateGameResult result = createGame(authToken, "game name test");

        assertNotNull(result);
        assertEquals(1, result.gameID());
    }

    @Test
    void createGameNegative() throws Exception {
        CreateGameRequest request = new CreateGameRequest("fake auth 123", "game name test");

        assertThrows(UnauthorizedException.class, () -> {
            gameService.createGame(request);
        });
    }

    @Test
    void joinGamePositive() throws Exception {
        CreateGameResult createGameResult = createGame(authToken, "game name test");
        JoinGameRequest joinGameRequest = new JoinGameRequest(
                authToken, "white", createGameResult.gameID());

        assertDoesNotThrow(() -> gameService.joinGame(joinGameRequest));

        gameService.joinGame(joinGameRequest);
        GameData updated = gameDAO.getGame(createGameResult.gameID());
        assertEquals("user123", updated.whiteUsername());
    }

    @Test
    void joinGameNegative() throws Exception {
        CreateGameResult createGameResult = createGame(authToken, "game name test");
        JoinGameRequest joinGameRequest = new JoinGameRequest(
                "fake auth", "black", createGameResult.gameID());

        assertThrows(UnauthorizedException.class, () -> {
            gameService.joinGame(joinGameRequest);
        });
    }
}
