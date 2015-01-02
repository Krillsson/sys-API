package se.christianjensen.maintenance.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import de.undercouch.bson4jackson.BsonFactory;
import de.undercouch.bson4jackson.BsonModule;
import se.christianjensen.maintenance.representation.internal.Preferences;

import java.io.*;

public class FileHelper {

    public static <Type> void saveObjectToFile(Object object, TypeReference<Type> type, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, Charsets.UTF_8.name());
            Writer writer = new BufferedWriter(outputStreamWriter);
            mapper.writerWithType(type).writeValue(writer, object);
            fileOutputStream.close();
            outputStreamWriter.close();
            writer.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static <ReturnedObject> ReturnedObject readObjectFromFile(String fileName, Class<ReturnedObject> returnedObjectClass) {
        ReturnedObject object = null;
        ObjectMapper mapper = new ObjectMapper();
        final JavaType type = mapper.getTypeFactory().constructType(returnedObjectClass);
        try {
            File file = new File(fileName);
            file.createNewFile();
            object = mapper.readValue(Files.toString(file, Charsets.UTF_8), type);
        } catch (FileNotFoundException exception) {
            //exception.printStackTrace();
        } catch (JsonMappingException exception) {
            //
        } catch (IOException exception) {
            //exception.printStackTrace();
        }
        return object;
    }

    public static void savePreferencesToBsonFile(Preferences preferences, String fileName) {
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

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static Preferences readPreferencesFromBsonFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper(new BsonFactory());
        mapper.registerModule(new BsonModule());

        ObjectReader reader = mapper.reader(Preferences.class);

        File inputFile = new File(fileName);
        InputStream inputStream;
        try {
            inputFile.createNewFile();
            inputStream = new FileInputStream(inputFile);
            return (Preferences) reader.readValue(inputStream);
        } catch (FileNotFoundException exception) {
            //exception.printStackTrace();
        } catch (JsonMappingException exception) {
            //
        } catch (IOException exception) {
            //exception.printStackTrace();
        }
        return null;
    }

    public static boolean fileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public static void deleteFile(String fileName) {
        if (fileExists(fileName)) {
            File file = new File(fileName);
            file.delete();
        }
    }
}
