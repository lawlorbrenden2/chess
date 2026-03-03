package dataaccess;

import model.data.AuthData;

import java.util.List;


public interface AuthDAO {
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    List<AuthData> listAuths() throws DataAccessException;
    void clear() throws DataAccessException;
}
