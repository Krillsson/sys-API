package se.christianjensen.maintenance.auth;

import org.mindrot.jbcrypt.BCrypt;
import se.christianjensen.maintenance.preferences.JsonPreferences;
import se.christianjensen.maintenance.representation.internal.Preferences;
import se.christianjensen.maintenance.representation.internal.User;

import java.util.List;

public class UserDAO {
    private JsonPreferences jsonPreferences;

    public UserDAO() {
        jsonPreferences = JsonPreferences.getInstance();
    }

    public User getUserByName(String name) {
        List<User> users = jsonPreferences.getPreferences().getUsers();
        for(User user : users)
        {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public void createUser(String name, String password) {
        if(getUserByName(name) == null) {
            User user = new User(name);
            user.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));

            Preferences preferences = jsonPreferences.getPreferences();
            preferences.getUsers().add(user);
            jsonPreferences.savePreferences(preferences);
        }
    }
}
