package dataaccess.sqldao;

import dataaccess.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfigurer {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                json TEXT DEFAULT NULL,
                PRIMARY KEY (username)
            )
            """,

            """
            CREATE TABLE IF NOT EXISTS auths (
                authToken VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL,
                json TEXT DEFAULT NULL,
                PRIMARY KEY (authToken)
            )
            """,

            """
            CREATE TABLE IF NOT EXISTS games (
                gameID INT NOT NULL AUTO_INCREMENT,
                gameName VARCHAR(255),
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                json TEXT,
                PRIMARY KEY (gameID)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
