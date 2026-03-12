package dataaccess.sqldao;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.data.GameData;

import java.util.List;

public class SQLGameDAO extends BaseSQLDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        DatabaseConfigurer.configureDatabase();
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        var statement =
                "INSERT INTO games (gameID, gameName, whiteUsername, blackUsername, json) VALUES (?, ?, ?, ?, ?)";
        String json = new Gson().toJson(game);

        return executeUpdate(statement, game.gameName(), game.whiteUsername(), game.blackUsername(), json);
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
