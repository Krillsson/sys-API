package se.christianjensen.maintenance.db;

import org.mindrot.jbcrypt.BCrypt;
import se.christianjensen.maintenance.representation.internal.User;

import java.util.List;

public class UserDAO {
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";

    List<User> users;

    public UserDAO() {
        users = JsonPreferences.getInstance().getPreferences().getUsers();
        if (users.isEmpty()) {
            createUser(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        }
    }

    public User getUserByName(String name) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public void createUser(String name, String password) {
        if (getUserByName(name) == null) {
            User user = new User(name);
            user.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
            users.add(user);
            JsonPreferences.getInstance().persistPreferences();
        }
    }
}
