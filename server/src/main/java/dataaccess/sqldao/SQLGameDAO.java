package dataaccess.sqldao;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.data.GameData;

import java.util.List;

public class SQLGameDAO implements GameDAO {
    @Override
    public int createGame(GameData game) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
