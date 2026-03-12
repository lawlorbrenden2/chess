package dataaccess.sqldao;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.data.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLUserDAO extends BaseSQLDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        DatabaseConfigurer.configureDatabase();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        var statement = "INSERT INTO users (username, password, email, json) VALUES (?, ?, ?, ?)";
        String json = new Gson().toJson(user);
        executeUpdate(statement, user.username(), hashedPassword, user.email(), json);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT password, email, json FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String hashedPassword = rs.getString("password");
                        String email = rs.getString("email");
                        String json = rs.getString("json");
                        UserData user = new Gson().fromJson(json, UserData.class);
                        return new UserData(user.username(), hashedPassword, email);
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
        var result = new ArrayList<UserData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT password, email, json FROM users";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String hashedPassword = rs.getString("password");
                        String email = rs.getString("email");
                        String json = rs.getString("json");
                        UserData user = new Gson().fromJson(json, UserData.class);
                        result.add(new UserData(user.username(), hashedPassword, email));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
        return result;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE users";
        executeUpdate(statement);
    }
}
