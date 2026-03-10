package dataaccess.sqldao;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.data.UserData;

import java.util.List;

public class SQLUserDAO implements UserDAO {
    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public List<UserData> listUsers() throws DataAccessException {
        return List.of();
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
