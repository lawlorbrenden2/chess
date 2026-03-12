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
    void createGameNegative() throws DataAccessException {
        GameData gameData = new GameData(1, "myWhiteUser",
                "myBlackUser", "game1");
        gameDAO.createGame(gameData);
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(gameData));
    }

    @Test
    void getGamePositive() throws DataAccessException {
        GameData gameData = new GameData(1, "myWhiteUser",
                "myBlackUser", "game1");
        gameDAO.createGame(gameData);
        assertDoesNotThrow(() -> gameDAO.getGame(gameData.gameID()));
    }

    @Test
    void getGameNegative() throws DataAccessException {
        GameData gameData = new GameData(1, "myWhiteUser",
                "myBlackUser", "game1");
        assertDoesNotThrow(() -> gameDAO.getGame(gameData.gameID()));
    }

    @Test
    void listGamesPositive() throws DataAccessException {
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
    void listGamesNegative() throws DataAccessException {
        var games = gameDAO.listGames();
        assertNotNull(games);
        assertEquals(0, games.size());
    }

    @Test
    void updateGamesPositive() throws DataAccessException {
        GameData oldGameData = new GameData(1, "myWhiteUser",
                "myBlackUser", "game1");
        gameDAO.createGame(oldGameData);

        GameData newGameData = new GameData(1, "myNewWhiteUser",
                "myBlackUser", "game1");

        assertDoesNotThrow(() -> gameDAO.updateGame(newGameData));
    }

    @Test
    void updateGamesNegative() throws DataAccessException {
        GameData newGameData = new GameData(1, "myNewWhiteUser",
                "myBlackUser", "game1");

        assertDoesNotThrow(() -> gameDAO.updateGame(newGameData));
    }

    @Test
    void clearGames() throws DataAccessException {
        GameData gameData1 = new GameData(1, "myWhiteUser1",
                "myBlackUser1", "game1");
        GameData gameData3 = new GameData(3, "myWhiteUser3",
                "myBlackUser3", "game3");

        gameDAO.createGame(gameData1);
        gameDAO.createGame(gameData3);

        assertDoesNotThrow(() -> gameDAO.clear());
    }

}
