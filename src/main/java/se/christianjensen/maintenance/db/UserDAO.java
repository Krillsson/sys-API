package se.christianjensen.maintenance.db;

import org.mindrot.jbcrypt.BCrypt;
import se.christianjensen.maintenance.representation.internal.User;

import java.util.List;

public class UserDAO {
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";

    private List<User> users;

    public UserDAO() {
        this.users = setUsers();
        if (users.isEmpty()) {
            create(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        }
    }

    public User getByName(String name) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public void create(String name, String password) {
        if (getByName(name) == null) {
            User user = new User(name);
            user.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
            users.add(user);
            persistUserStore();
        } else {
            throw new IllegalArgumentException("User already exists");
        }
    }

    public void update(User user) {
        if (user.getUuid() == null) {
            throw new IllegalArgumentException("Unsufficient rights to modify user");
        }
        Boolean found = false;
        for (User u : users) {
            if (u.getUuid().equals(user.getUuid())) {
                u.setName(user.getName());
                u.setPasswordHash(u.getPasswordHash());
                persistUserStore();
                found = true;
            }
        }
        if (found == false) {
            throw new IllegalArgumentException("User not found");
        }
    }

    protected List<User> setUsers() {
        return BsonPreferences.getInstance().getPreferences().getUsers();
    }

    protected void persistUserStore() {
        BsonPreferences.getInstance().persistPreferences();
    }
}
