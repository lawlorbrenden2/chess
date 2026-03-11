package dataaccess.sqldao;

import dataaccess.DataAccessException;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public abstract class BaseSQLDAO {
    protected int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    ps.setObject(i + 1, param);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to update database: " + e.getMessage());
        }
    }
}
