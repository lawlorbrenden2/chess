package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;

import model.data.AuthData;
import model.data.UserData;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.RegisterResult;
import service.exceptions.*;

import java.util.UUID;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException, BadRequestException, DataAccessException {
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequestException("Error: Bad request");
        }

        // check if username already exists
        if (userDAO.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Error: Already taken");
        }

        // create user data
        UserData user = new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(user);

        // generate auth token
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, request.username());
        authDAO.createAuth(auth);

        // return register result
        return new RegisterResult(user.username(), token);
    }

    public LoginResult login(LoginRequest request) throws BadRequestException, UnauthorizedException, DataAccessException {
        if (request.username() == null || request.password() == null) {
            throw new BadRequestException("Error: Bad request");
        }

        UserData user = userDAO.getUser(request.username());

        if (user == null || !user.password().equals(request.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, request.username());
        authDAO.createAuth(auth);

        return new LoginResult(user.username(), token);
    }

    public void logout(LogoutRequest request) throws UnauthorizedException, DataAccessException {
        AuthData auth = authDAO.getAuth(request.authToken());

        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        authDAO.deleteAuth(auth.authToken());
    }
}
