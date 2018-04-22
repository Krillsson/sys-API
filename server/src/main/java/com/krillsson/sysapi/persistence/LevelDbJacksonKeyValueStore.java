package com.krillsson.sysapi.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.boon.slumberdb.leveldb.LevelDBKeyValueStore;

public class LevelDbJacksonKeyValueStore<V> extends JacksonKeyValueStore<V> {
    public LevelDbJacksonKeyValueStore(Class<V> cls, ObjectMapper objectMapper, String fileName) {
        super(new LevelDBKeyValueStore(fileName, null, true), cls, objectMapper);
    }
}
