package dataaccess;

import model.data.GameData;

import java.util.List;

public interface GameDAO {
    int createGame(GameData game) throws DataAccessException; // returns gameID
    GameData getGame(int gameID) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    void clear() throws DataAccessException;
}
