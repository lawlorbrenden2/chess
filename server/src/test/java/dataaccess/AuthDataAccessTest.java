package dataaccess;

import dataaccess.sqldao.SQLAuthDAO;
import model.data.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthDataAccessTest {

    private AuthDAO authDAO;

    @BeforeEach
    void setUp() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        authDAO.clear();
    }

    @Test
    void createAuthPositive() throws DataAccessException {
        AuthData authData = new AuthData("my1token23", "bradleyWaddle");
        assertDoesNotThrow(() -> authDAO.createAuth(authData));
    }

    @Test
    void createAuthNegative() throws DataAccessException {
        AuthData authData = new AuthData("my1token23", "bradleyWaddle");
        authDAO.createAuth(authData);
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(authData));
    }

    @Test
    void getAuthPositive() throws DataAccessException {
        AuthData authData = new AuthData("my1token23", "bradleyWaddle");
        authDAO.createAuth(authData);
        assertDoesNotThrow(() -> authDAO.getAuth(authData.authToken()));
    }

    @Test
    void getAuthNegative() throws DataAccessException {
        AuthData authData = new AuthData("my1token23", "bradleyWaddle");
        assertDoesNotThrow(() -> authDAO.getAuth(authData.authToken()));
    }

    @Test
    void deleteAuthPositive() throws DataAccessException {
        AuthData authData = new AuthData("my1token23", "bradleyWaddle");
        authDAO.createAuth(authData);
        assertDoesNotThrow(() -> authDAO.deleteAuth(authData.authToken()));
    }

    @Test
    void deleteAuthNegative() throws DataAccessException {
        AuthData authData = new AuthData("my1token23", "bradleyWaddle");
        assertDoesNotThrow(() -> authDAO.getAuth(authData.authToken()));
    }

    @Test
    void listAuthsPositive() throws DataAccessException {
        AuthData authData1 = new AuthData("my1token23", "bradleyWaddle");
        AuthData authData2 = new AuthData("my1token234", "jcdizzy");
        AuthData authData3 = new AuthData("my1towi3t45n23", "ILikeCheese");

        authDAO.createAuth(authData1);
        authDAO.createAuth(authData2);
        authDAO.createAuth(authData3);


        assertDoesNotThrow(() -> authDAO.listAuths());
    }

    @Test
    void listAuthsNegative() throws DataAccessException {
        var auths = authDAO.listAuths();
        assertNotNull(auths);
        assertEquals(0, auths.size());
    }

    @Test
    void clearAuths() throws DataAccessException {
        AuthData authData1 = new AuthData("my1token23", "bradleyWaddle");
        AuthData authData3 = new AuthData("my1towi3t45n23", "ILikeCheese");

        authDAO.createAuth(authData1);
        authDAO.createAuth(authData3);

        assertDoesNotThrow(() -> authDAO.clear());
    }

}

