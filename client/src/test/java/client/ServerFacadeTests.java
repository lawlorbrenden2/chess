package client;

import model.data.UserData;
import model.request.*;
import model.result.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacade;
    static UserData user;

    @BeforeAll
    public static void init() {
        server = new Server();
        String port = String.valueOf(server.run(0));
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void setUp() throws Exception {
        serverFacade.clear();
        user = new UserData("brad", "brad'sPass", "brad'sEmail");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPositive() throws Exception {
        RegisterRequest request = new RegisterRequest(user.username(), user.password(), user.email());
        RegisterResult result = serverFacade.register(request);
        assertNotNull(result.authToken());
    }

    @Test
    public void registerNegative() throws Exception {
        RegisterRequest request = new RegisterRequest(user.username(), user.password(), user.email());
        serverFacade.register(request);
        assertThrows(Exception.class, () -> serverFacade.register(request));
    }

    @Test
    public void loginPositive() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        serverFacade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());
        assertDoesNotThrow(() -> serverFacade.login(loginRequest));
    }

    @Test
    public void loginNegative() {
        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());
        assertThrows(Exception.class, () -> serverFacade.login(loginRequest));
    }

    @Test
    public void logoutPositive() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        serverFacade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());
        LoginResult loginResult = serverFacade.login(loginRequest);
        serverFacade.setAuthToken(loginResult.authToken());

        assertDoesNotThrow(() -> serverFacade.logout());
    }

    @Test
    public void logoutNegative() {
        serverFacade.setAuthToken("faketoken");
        assertThrows(Exception.class, () -> serverFacade.logout());
    }

    @Test
    public void listGamesPositive() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        serverFacade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());
        LoginResult loginResult = serverFacade.login(loginRequest);
        serverFacade.setAuthToken(loginResult.authToken());

        CreateGameRequest createGameRequest1 =
                new CreateGameRequest(loginResult.authToken(), "gameName1");
        CreateGameRequest createGameRequest2 =
                new CreateGameRequest(loginResult.authToken(), "gameName2");
        CreateGameRequest createGameRequest3 =
                new CreateGameRequest(loginResult.authToken(), "gameName3");

        serverFacade.createGame(createGameRequest1);
        serverFacade.createGame(createGameRequest2);
        serverFacade.createGame(createGameRequest3);

        assertDoesNotThrow(() -> serverFacade.listGames());
    }

    @Test
    public void listGamesNegative() {
        serverFacade.setAuthToken("nfake token");
        assertThrows(Exception.class, () -> serverFacade.listGames());
    }

    @Test
    public void createGamePositive() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        serverFacade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());
        LoginResult loginResult = serverFacade.login(loginRequest);
        serverFacade.setAuthToken(loginResult.authToken());

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "gameName1");

        assertDoesNotThrow(() -> serverFacade.createGame(createGameRequest));
    }

    @Test
    public void createGameNegative() {
        serverFacade.setAuthToken("bad_token");
        CreateGameRequest createGameRequest = new CreateGameRequest(null, "gameName1");
        assertThrows(Exception.class, () -> serverFacade.createGame(createGameRequest));
    }

    @Test
    public void joinGamePositive() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        serverFacade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());
        LoginResult loginResult = serverFacade.login(loginRequest);
        serverFacade.setAuthToken(loginResult.authToken());

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "gameName1");
        CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);

        JoinGameRequest joinGameRequest =
                new JoinGameRequest(loginResult.authToken(), "WHITE", createGameResult.gameID());

        assertDoesNotThrow(() -> serverFacade.joinGame(joinGameRequest));
    }

    @Test
    public void joinGameNegative() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        serverFacade.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());
        LoginResult loginResult = serverFacade.login(loginRequest);
        serverFacade.setAuthToken(loginResult.authToken());

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "gameName1");
        CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);

        serverFacade.setAuthToken("evilToken");
        JoinGameRequest joinGameRequest = new JoinGameRequest(null, "WHITE", createGameResult.gameID());

        assertThrows(Exception.class, () -> serverFacade.joinGame(joinGameRequest));
    }


    @Test
    public void clear() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        serverFacade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());
        LoginResult loginResult = serverFacade.login(loginRequest);
        serverFacade.setAuthToken(loginResult.authToken());

        CreateGameRequest createGameRequest1 =
                new CreateGameRequest(loginResult.authToken(), "gameName1");
        CreateGameRequest createGameRequest2 =
                new CreateGameRequest(loginResult.authToken(), "gameName2");
        CreateGameRequest createGameRequest3 =
                new CreateGameRequest(loginResult.authToken(), "gameName3");

        serverFacade.createGame(createGameRequest1);
        serverFacade.createGame(createGameRequest2);
        serverFacade.createGame(createGameRequest3);

        assertDoesNotThrow(() -> serverFacade.clear());
    }
}
