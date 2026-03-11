package dataaccess.sqldao;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.data.UserData;

import java.util.List;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        DatabaseConfigurer.configureDatabase();
    }


    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        String json = new Gson().toJson(user);

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
