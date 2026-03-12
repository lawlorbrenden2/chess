package dataaccess;

import dataaccess.sqldao.SQLUserDAO;
import model.data.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class UserDataAccessTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = new SQLUserDAO();
        userDAO.clear();
    }

    @Test
    void createUserPositive() throws DataAccessException {
        UserData userData = new UserData("brad", "pass67", "bradsemail@email.com");
        assertDoesNotThrow(() -> userDAO.createUser(userData));
    }

    @Test
    void createUserNegative() throws DataAccessException {
        UserData userData = new UserData("brad", "pass67", "bradsemail@email.com");
        userDAO.createUser(userData);
        assertThrows(DataAccessException.class, () -> userDAO.createUser(userData));
    }

    @Test
    void getUserPositive() throws DataAccessException {
        UserData userData = new UserData("brad", "pass67", "bradsemail@email.com");
        userDAO.createUser(userData);
        assertDoesNotThrow(() -> userDAO.getUser(userData.username()));
    }

    @Test
    void getUserNegative() throws DataAccessException {
        UserData userData = new UserData("brad", "pass67", "bradsemail@email.com");
        assertDoesNotThrow(() -> userDAO.getUser(userData.username()));
    }

    @Test
    void listUsersPositive() throws DataAccessException {
        UserData userData1 = new UserData("brad", "pass67", "bradsemail@email.com");
        UserData userData2 = new UserData("brandon", "iHeartAhri", "bradsemail2@email.com");
        UserData userData3 = new UserData("brandonRoberts", "notobvpassword",
                "bradsemail3@email.com");

        userDAO.createUser(userData1);
        userDAO.createUser(userData2);
        userDAO.createUser(userData3);

        assertDoesNotThrow(() -> userDAO.listUsers());
    }

    @Test
    void listUsersNegative() throws DataAccessException {
        var users = userDAO.listUsers();
        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    void clearUsers() throws DataAccessException {
        UserData userData1 = new UserData("brad", "pass67", "bradsemail@email.com");
        UserData userData2 = new UserData("brandonRoberts", "notobvpassword",
                "bradsemail3@email.com");

        userDAO.createUser(userData1);
        userDAO.createUser(userData2);

        assertDoesNotThrow(() -> userDAO.clear());
    }

}
