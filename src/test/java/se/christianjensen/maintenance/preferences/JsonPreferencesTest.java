package se.christianjensen.maintenance.preferences;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.christianjensen.maintenance.representation.internal.User;
import util.MixedKey;

public class JsonPreferencesTest {

    private JsonPreferences jsonPreferences;
    private JsonPreferences.Preferences preferences;
    private MixedKey<User> key;
    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User("asd","asd");
        key = new MixedKey<>(User.class, "Users");
        jsonPreferences = JsonPreferences.getInstance();
    }

    @Test
    public void testName() throws Exception {
        preferences = jsonPreferences.getPreferences();
        preferences.getUsers().add(user);
        jsonPreferences.savePreferences(preferences);
    }

    @After
    public void tearDown() throws Exception {
        FileHelper.deleteFile("preferences.json");
    }
}