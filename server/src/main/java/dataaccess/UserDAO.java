package dataaccess;

import model.data.UserData;

import java.util.List;

public interface UserDAO {
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    List<UserData> listUsers() throws DataAccessException;
    void clear() throws DataAccessException;
}
