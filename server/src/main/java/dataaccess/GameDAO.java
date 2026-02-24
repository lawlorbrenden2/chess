package dataaccess;

import model.*;

public interface GameDAO {
    int createGame(GameData game) throws DataAccessException; // returns gameID
    GameData getGame(int gameID) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    void clearGames() throws DataAccessException;
}
