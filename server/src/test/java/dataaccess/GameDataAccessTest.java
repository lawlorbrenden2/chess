package dataaccess;

import dataaccess.sqldao.SQLGameDAO;
import model.data.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameDataAccessTest {
    private GameDAO gameDAO;

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO = new SQLGameDAO();
        gameDAO.clear();
    }

    @Test
    void createGamePositive() throws DataAccessException {
        GameData gameData = new GameData(1, "myWhiteUser",
                            "myBlackUser", "game1");
        assertDoesNotThrow(() -> gameDAO.createGame(gameData));
    }

    @Test
    void createUserNegative() throws DataAccessException {
        GameData gameData = new GameData(1, "myWhiteUser",
                "myBlackUser", "game1");
        gameDAO.createGame(gameData);
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(gameData));
    }

    @Test
    void getUserPositive() throws DataAccessException {
        GameData gameData = new GameData(1, "myWhiteUser",
                "myBlackUser", "game1");
        gameDAO.createGame(gameData);
        assertDoesNotThrow(() -> gameDAO.getGame(gameData.gameID()));
    }

    @Test
    void getUserNegative() throws DataAccessException {
        GameData gameData = new GameData(1, "myWhiteUser",
                "myBlackUser", "game1");
        assertDoesNotThrow(() -> gameDAO.getGame(gameData.gameID()));
    }

    @Test
    void listUsersPositive() throws DataAccessException {
        GameData gameData1 = new GameData(1, "myWhiteUser1",
                "myBlackUser1", "game1");
        GameData gameData2 = new GameData(2, "myWhiteUser2",
                "myBlackUser2", "game2");
        GameData gameData3 = new GameData(3, "myWhiteUser3",
                "myBlackUser3", "game3");

        gameDAO.createGame(gameData1);
        gameDAO.createGame(gameData2);
        gameDAO.createGame(gameData3);

        assertDoesNotThrow(() -> gameDAO.listGames());
    }

    @Test
    void listUsersNegative() throws DataAccessException {
        var games = gameDAO.listGames();
        assertNotNull(games);
        assertEquals(0, games.size());
    }

    @Test
    void clearUsers() throws DataAccessException {
        GameData gameData1 = new GameData(1, "myWhiteUser1",
                "myBlackUser1", "game1");
        GameData gameData3 = new GameData(3, "myWhiteUser3",
                "myBlackUser3", "game3");

        gameDAO.createGame(gameData1);
        gameDAO.createGame(gameData3);

        assertDoesNotThrow(() -> gameDAO.clear());
    }

}
