package client;

import model.data.UserData;
import model.request.*;
import model.result.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacade;
    static UserData user;

    @BeforeAll
    public static void init() {
        server = new Server();
        String port = String.valueOf(server.run(0));
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
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
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void registerNegative() throws Exception {
        RegisterRequest request = new RegisterRequest(user.username(), user.password(), user.email());
        serverFacade.register(request);
        Assertions.assertThrows(Exception.class, () -> serverFacade.register(request));
    }

    @Test
    public void loginPositive() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        serverFacade.register(registerRequest);

    }

    @Test
    public void loginNegative() throws Exception {

    }

    @Test
    public void logoutPositive() throws Exception {

    }

    @Test
    public void logoutNegative() throws Exception {

    }

    @Test
    public void listGamesPositive() throws Exception {

    }

    @Test
    public void listGamesNegative() throws Exception {

    }

    @Test
    public void createGamePositive() throws Exception {

    }

    @Test
    public void createGameNegative() throws Exception {

    }

    @Test
    public void joinGamePositive() throws Exception {

    }

    @Test
    public void joinGameNegative() throws Exception {

    }

    @Test
    public void clear() throws Exception {

    }

}
