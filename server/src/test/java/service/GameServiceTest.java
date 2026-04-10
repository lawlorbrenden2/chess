package service;

import dataaccess.*;

import dataaccess.sqldao.SQLAuthDAO;
import dataaccess.sqldao.SQLGameDAO;
import dataaccess.sqldao.SQLUserDAO;
import model.data.GameData;
import model.request.*;
import model.result.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exceptions.UnauthorizedException;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private GameService gameService;
    private String authToken;
    private GameDAO gameDAO;

    @BeforeEach
    void setUp() throws Exception {
        UserDAO userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        AuthDAO authDAO = new SQLAuthDAO();
        UserService userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);


        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();

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
        CreateGameResult result1 = createGame(authToken, "game1");
        CreateGameResult result2 = createGame(authToken, "game2");
        CreateGameResult result3 = createGame(authToken, "game3");

        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);

        assertEquals(3, listGamesResult.games().size());

        assertEquals(result1.gameID(), listGamesResult.games().get(0).gameID());
        assertEquals("game1", listGamesResult.games().get(0).gameName());

        assertEquals(result2.gameID(), listGamesResult.games().get(1).gameID());
        assertEquals("game2", listGamesResult.games().get(1).gameName());

        assertEquals(result3.gameID(), listGamesResult.games().get(2).gameID());
        assertEquals("game3", listGamesResult.games().get(2).gameName());
    }

    @Test
    void listGamesNegative() throws Exception {
        createGame(authToken, "game1");
        createGame(authToken, "game2");
        createGame(authToken, "game3");

        ListGamesRequest listGamesRequest = new ListGamesRequest("I am not real hahahahahaha");

        assertThrows(UnauthorizedException.class, () -> {
            gameService.listGames(listGamesRequest);
        });
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
                authToken, "wHiTe", createGameResult.gameID());

        assertDoesNotThrow(() -> gameService.joinGame(joinGameRequest));

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
