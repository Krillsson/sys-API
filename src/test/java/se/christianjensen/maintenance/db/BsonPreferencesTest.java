package se.christianjensen.maintenance.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.christianjensen.maintenance.representation.internal.Preferences;
import se.christianjensen.maintenance.representation.internal.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BsonPreferencesTest {

    BsonPreferences bsonPreferences = BsonPreferences.getInstance();

    @Before
    public void setUp() throws Exception {
        bsonPreferences.preferences = null;
        deletePreferencesFile();
    }

    @Test
    public void nonExistingFileShouldNotFail() throws Exception {
        Preferences preferences = bsonPreferences.getPreferences();
        List<User> users = preferences.getUsers();
        assertThat(users.isEmpty(), is(true));
    }

    @Test
    public void youShouldBeAbleToGetStuffYouPutIn() throws Exception {
        Preferences preferences = bsonPreferences.getPreferences();

        long date = new Date().getTime();
        User user = new User("Derp", "asjkdh234");
        User secondUser = new User("Derpina", "aklsdjasdjkl123");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        preferences.setSavedTimeStamp(new Date(date));
        preferences.setUsers(users);
        preferences.getUsers().add(secondUser);

        //Persist to file
        bsonPreferences.persistPreferences();
        //Then reset the internal instance
        bsonPreferences.preferences = null;
        //This should then contain the preferences from file
        Preferences newPreferences = bsonPreferences.getPreferences();

        assertThat(preferences.getUsers().get(0).getName(), equalTo(user.getName()));
        assertThat(preferences.getUsers().get(1).getName(), equalTo(secondUser.getName()));
        assertThat(preferences.getSavedTimeStamp(), is(not(date)));
    }

    @After
    public void tearDown() throws Exception {
        bsonPreferences.preferences = null;
        deletePreferencesFile();
    }

    private void deletePreferencesFile() {
        File file = new File("db.bson");
        if (file.exists()) {
            file.delete();
        }
    }


}