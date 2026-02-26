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

import java.util.UUID;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException, DataAccessException {
        // check if username already exists
        if (userDAO.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Username already taken");
        }

        // create user data
        UserData user = new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(user);

        // generate auth token
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, request.username());
        authDAO.createAuth(auth);

        // return register result
        return new RegisterResult(request.username(), token);
    }

    public LoginResult login(LoginRequest loginRequest) {
        return new LoginResult("user", "auth");
    }
    public void logout(LogoutRequest logoutRequest) {}
}
