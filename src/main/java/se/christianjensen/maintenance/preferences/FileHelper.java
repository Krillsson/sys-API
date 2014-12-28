package se.christianjensen.maintenance.preferences;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.base.Charsets;
import com.google.common.io.Files;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
        } catch (JsonMappingException exception)
        {
            //
        } catch (IOException exception) {
            //exception.printStackTrace();
        }
        return object;
    }

    public static <ReturnedObject> List<ReturnedObject> readObjectListFromFile(String fileName, Class<ReturnedObject> returnedObjectClass) {
        List<ReturnedObject> objectList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        final CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, returnedObjectClass);
        try {
            objectList = mapper.readValue(Files.toString(new File(fileName), Charsets.UTF_8), collectionType);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return objectList;
    }

    public static boolean fileExists(String fileName) {
        File file =  new File(fileName);
        return file.exists();
    }

    public static void deleteFile(String fileName) {
        if(fileExists(fileName))
        {
            File file = new File(fileName);
            file.delete();
        }
    }
}
