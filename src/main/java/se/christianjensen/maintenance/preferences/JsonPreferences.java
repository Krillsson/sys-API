package se.christianjensen.maintenance.preferences;

import com.fasterxml.jackson.core.type.TypeReference;
import se.christianjensen.maintenance.representation.internal.Preferences;

import java.util.Date;

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

}
