package se.christianjensen.maintenance.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import de.undercouch.bson4jackson.BsonFactory;
import de.undercouch.bson4jackson.BsonModule;
import org.slf4j.Logger;
import se.christianjensen.maintenance.representation.internal.Preferences;

import java.io.*;
import java.util.Date;

public class BsonPreferences {

    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BsonPreferences.class.getSimpleName());

    private static final String PATH = "db.bson";
    protected Preferences preferences;

    private BsonPreferences() {
        //Singleton
    }

    private static final BsonPreferences instance = new BsonPreferences();

    public static BsonPreferences getInstance() {
        return instance;
    }

    public Preferences getPreferences() {
        if (preferences == null) {
            readPreferences();
        }
        return preferences;
    }

    private void readPreferences() {
        preferences = readPreferencesFromBsonFile(PATH);
        if (preferences == null) {
            preferences = new Preferences();
        }
    }

    public void persistPreferences() {
        preferences.setSavedTimeStamp(new Date());
        savePreferencesToBsonFile(preferences, PATH);
    }


    private void savePreferencesToBsonFile(Preferences preferences, String fileName) {
        File outputFile = new File(fileName);
        if (outputFile.isFile()) {
            outputFile.delete();
        }
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFile);
            ObjectMapper mapper = new ObjectMapper(new BsonFactory());
            mapper.registerModule(new BsonModule());
            mapper.writeValue(outputStream, preferences);
        } catch (IOException exception) {
            LOGGER.error("Unable to save preferences: " + exception.getMessage(), exception);
        }
    }

    private Preferences readPreferencesFromBsonFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper(new BsonFactory());
        mapper.registerModule(new BsonModule());
        ObjectReader reader = mapper.reader(Preferences.class);
        File inputFile = new File(fileName);
        InputStream inputStream;
        try {
            inputFile.createNewFile();
            inputStream = new FileInputStream(inputFile);
            return (Preferences) reader.readValue(inputStream);
        } catch (IOException exception) {
            //Swallow exception. If the file does not exist yet, we just create a new instance in memory
        }
        return null;
    }
}
