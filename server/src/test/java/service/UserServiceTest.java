package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.sqldao.SQLAuthDAO;
import dataaccess.sqldao.SQLUserDAO;
import model.request.*;
import model.result.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exceptions.AlreadyTakenException;
import service.exceptions.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;
    private AuthDAO authDAO;

    private final String testUsername = "user123";
    private final String testPassword = "pass67";
    private final String testEmail = "my_email@byu.edu";

    @BeforeEach
    void setUp() throws DataAccessException {
        UserDAO userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        userService = new UserService(userDAO, authDAO);

        userDAO.clear();
        authDAO.clear();
    }

    private RegisterResult registerTestUser() throws Exception {
        RegisterRequest request = new RegisterRequest(testUsername, testPassword, testEmail);
        return userService.register(request);
    }

    private LoginResult loginTestUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest(testUsername, testPassword);
        return userService.login(loginRequest);
    }

    @Test
    void registerPositive() throws Exception {
        RegisterResult result = registerTestUser();
        assertEquals(testUsername, result.username());
        assertNotNull(result.authToken());
    }

    @Test
    void registerNegative() throws Exception {
        registerTestUser();
        assertThrows(AlreadyTakenException.class, this::registerTestUser);
    }

    @Test
    void loginPositive() throws Exception {
        registerTestUser();
        LoginResult result = loginTestUser();
        assertEquals(testUsername, result.username());
        assertNotNull(result.authToken());
    }

    @Test
    void loginNegative() {
        LoginRequest request = new LoginRequest("idonotexist123hello", "pass123");
        assertThrows(UnauthorizedException.class, () -> userService.login(request));
    }

    @Test
    void logoutPositive() throws Exception {
        registerTestUser();
        LoginResult loginResult = loginTestUser();

        LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
        assertDoesNotThrow(() -> userService.logout(logoutRequest));
        assertNull(authDAO.getAuth(loginResult.authToken()));
    }

    @Test
    void logoutNegative() {
        LogoutRequest request = new LogoutRequest("invalid-token-452");
        assertThrows(UnauthorizedException.class, () -> userService.logout(request));
    }
}