package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.request.ClearRequest;
import model.result.ClearResult;

public record ClearService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {

    public ClearResult clear(ClearRequest clearRequest) throws DataAccessException {
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();

        return new ClearResult(true);
    }
}
