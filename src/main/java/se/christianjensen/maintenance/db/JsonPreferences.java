package se.christianjensen.maintenance.db;

import se.christianjensen.maintenance.representation.internal.Preferences;

import java.util.Date;

public class JsonPreferences {

    private static final String PATH = "db.bson";
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
        if (preferences == null) {
            readPreferences();
        }
        return preferences;
    }

    public void savePreferences(Preferences preferences)
    {
        this.preferences = preferences;
        persistPreferences();
    }

    private void readPreferences()
    {
        //preferences = FileHelper.readObjectFromFile(PATH, Preferences.class);
        preferences = FileHelper.readPreferencesFromBsonFile(PATH);
        if(preferences == null)
        {
            preferences = new Preferences();
        }
    }

    public void persistPreferences() {
        preferences.setSavedTimeStamp(new Date());
        //FileHelper.saveObjectToFile(preferences, new TypeReference<Preferences>() {}, PATH);
        FileHelper.savePreferencesToBsonFile(preferences, PATH);
    }

}
