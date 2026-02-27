package service;


import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exceptions.AlreadyTakenException;

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
}
