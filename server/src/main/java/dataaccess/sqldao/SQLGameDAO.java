package dataaccess.sqldao;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.data.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO extends BaseSQLDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        DatabaseConfigurer.configureDatabase();
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        for (GameData gameData: listGames()) {
            if (gameData.gameName().equals(game.gameName())) {
                throw new DataAccessException("Already exists");
            }
        }

        var statement =
                "INSERT INTO games (gameName, whiteUsername, blackUsername, json) VALUES (?, ?, ?, ?)";
        String json = new Gson().toJson(game);

        return executeUpdate(statement, game.gameName(), game.whiteUsername(), game.blackUsername(), json);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM games WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String json = rs.getString("json");
                        return new Gson().fromJson(json, GameData.class);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String json = rs.getString("json");
                        result.add(new Gson().fromJson(json, GameData.class));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
        return result;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        var statement = "UPDATE games set gameName=?, whiteUsername=?, blackUsername=?, json=? WHERE gameID=?";
        String json = new Gson().toJson(game);
        executeUpdate(statement, game.gameName(), game.whiteUsername(), game.blackUsername(), json, game.gameID());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE games";
        executeUpdate(statement);
    }
}
