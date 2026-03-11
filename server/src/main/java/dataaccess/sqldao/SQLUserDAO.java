package dataaccess.sqldao;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.data.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SQLUserDAO extends BaseSQLDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        DatabaseConfigurer.configureDatabase();
    }


    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email, json) VALUES (?, ?, ?, ?)";
        String json = new Gson().toJson(user);
        executeUpdate(statement, user.username(), user.password(), user.email(), json);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String json = rs.getString("json");
                        return new Gson().fromJson(json, UserData.class);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<UserData> listUsers() throws DataAccessException {
        return List.of();
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
