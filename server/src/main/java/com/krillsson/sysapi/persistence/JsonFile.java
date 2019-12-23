package com.krillsson.sysapi.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krillsson.sysapi.dto.monitor.Monitor;
import com.krillsson.sysapi.dto.monitor.MonitorEvent;
import org.slf4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class JsonFile<T> {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(JsonFile.class);

    private final TypeReference<T> typeToken;
    private final ObjectMapper objectMapper;

    private final String filePath;
    private T ifNull;

    public static TypeReference<HashMap<String, Monitor>> mapTypeReference() {
        return new TypeReference<HashMap<String, Monitor>>() {
        };
    }

    public static TypeReference<List<MonitorEvent>> listTypeReference() {
        return new TypeReference<List<MonitorEvent>>() {
        };
    }

    public JsonFile(String filePath, TypeReference<T> typeToken, T ifNull, ObjectMapper objectMapper) {
        this.filePath = filePath;
        this.typeToken = typeToken;
        this.ifNull = ifNull;
        this.objectMapper = objectMapper;
    }

    public <R> R getPersistedData(boolean persistChanges, Result<T, R> result) {
        R value = null;
        File file = new File(filePath);
        try {
            //does not create a file if it already exists
            file.createNewFile();
        } catch (IOException e) {
            LOGGER.error("Unable to create file {}", filePath, e);
        }
        try (Reader reader = new FileReader(file)) {
            T jsonObject = null;
            if (file.length() > 0) {
                jsonObject = objectMapper.readValue(reader, typeToken);
            }
            if (jsonObject == null) {
                jsonObject = ifNull;
            }
            value = result.result(jsonObject);
            if (persistChanges) {
                try (Writer writer = new FileWriter(filePath)) {
                    LOGGER.debug("Writing {}", filePath);
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, jsonObject);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Exception while serializing/deserializing", e);
        }
        return value;
    }


    public interface Result<T, R> {
        R result(T value);
    }
}
