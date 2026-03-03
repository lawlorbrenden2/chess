package dataaccess;

import model.data.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException {
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public List<UserData> listUsers() throws DataAccessException {
        return new ArrayList<>(users.values());
    }

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }
}
