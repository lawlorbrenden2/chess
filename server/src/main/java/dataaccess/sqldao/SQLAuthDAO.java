package dataaccess.sqldao;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.data.AuthData;

import java.util.List;

public class SQLAuthDAO extends BaseSQLDAO implements AuthDAO {
    public SQLAuthDAO() throws DataAccessException {
        DatabaseConfigurer.configureDatabase();
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public List<AuthData> listAuths() throws DataAccessException {
        return List.of();
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
