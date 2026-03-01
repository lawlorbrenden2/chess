package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.request.*;
import model.result.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exceptions.AlreadyTakenException;
import service.exceptions.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void setUp() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }


    @Test
    void registerPositive() throws Exception {
        RegisterRequest request = new RegisterRequest("user123", "pass67", "my_email@byu.edu");
        RegisterResult result = userService.register(request);

        assertEquals("user123", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    void registerNegative() throws Exception {
        RegisterRequest request =
                new RegisterRequest("user123", "pass67", "my_email@byu.edu");

        userService.register(request);

        assertThrows(AlreadyTakenException.class, () -> {
            userService.register(request);
        });
    }

    @Test
    void loginPositive() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("user123", "pass67", "my_email@byu.edu");
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("user123", "pass67");
        LoginResult result = userService.login(loginRequest);

        assertEquals("user123", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    void loginNegative() throws Exception {
        LoginRequest request = new LoginRequest("idonotexist123hello", "pass123");
        assertThrows(UnauthorizedException.class, () -> {
            userService.login(request);
        });
    }

    @Test
    void logoutPositive() throws Exception {

    }

    @Test
    void logoutNegative() throws Exception {

    }
}
