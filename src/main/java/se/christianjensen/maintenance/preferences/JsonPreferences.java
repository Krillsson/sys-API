package se.christianjensen.maintenance.preferences;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.omg.CORBA.*;
import se.christianjensen.maintenance.representation.internal.User;
import util.MixedKey;
import util.MixedMap;

import java.io.File;
import java.lang.Object;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class JsonPreferences {

    private static final String PATH = "preferences.json";
    private Preferences preferences;

    private JsonPreferences(){
        //Singleton
    }
    private static final JsonPreferences instance = new JsonPreferences();
    public static JsonPreferences getInstance() {

        return instance;
    }

    public Preferences getPreferences()
    {
        readPreferences();
        return preferences;
    }

    public void savePreferences(Preferences preferences)
    {
        this.preferences = preferences;
        persistPreferences();
    }

    private void persistPreferences()
    {
        preferences.setSavedTimeStamp(new Date());
        FileHelper.saveObjectToFile(preferences, new TypeReference<Preferences>() {}, PATH);
    }

    private void readPreferences()
    {
        preferences = FileHelper.readObjectFromFile(PATH, Preferences.class);
        if(preferences == null)
        {
            preferences = new Preferences();
        }
    }

    public static class Preferences {
        private Date savedTimeStamp;
        private List<User> users = new ArrayList<>();

        public Date getSavedTimeStamp()
        {
            return savedTimeStamp;
        }

        public void setSavedTimeStamp(Date savedTimeStamp) {
            this.savedTimeStamp = savedTimeStamp;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }
}
