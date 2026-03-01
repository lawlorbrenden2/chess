package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTest {
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        gameService = new GameService(gameDAO, authDAO);
    }

    @Test
    void listGamesPositive() throws Exception {

    }

    @Test
    void listGamestNegative() throws Exception {

    }

    @Test
    void createGamePositive() throws Exception {

    }

    @Test
    void createGameNegative() throws Exception {

    }

    @Test
    void joinGamePositive() throws Exception {

    }

    @Test
    void joinGameNegative() throws Exception {

    }
}
