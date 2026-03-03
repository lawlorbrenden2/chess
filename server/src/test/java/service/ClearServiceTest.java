package service;

import dataaccess.*;
import model.request.ClearRequest;
import model.request.CreateGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;

import model.result.RegisterResult;
import org.junit.jupiter.api.Test;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;
import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {

    @Test
    void clearTest() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException {

        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);
        GameService gameService = new GameService(gameDAO, authDAO);
        ClearService clearService = new ClearService(gameDAO, userDAO, authDAO);

        RegisterRequest registerRequest = new RegisterRequest("abc", "123", "my_email.asu.edu");
        RegisterResult registerResult = userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(registerResult.username(), registerRequest.password());
        userService.login(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "game1");
        gameService.createGame(createGameRequest);

        assertFalse(userDAO.listUsers().isEmpty());
        assertFalse(gameDAO.listGames().isEmpty());
        assertFalse(authDAO.listAuths().isEmpty());

        clearService.clear(new ClearRequest());

        assertTrue(userDAO.listUsers().isEmpty());
        assertTrue(gameDAO.listGames().isEmpty());
        assertTrue(authDAO.listAuths().isEmpty());
    }
}
