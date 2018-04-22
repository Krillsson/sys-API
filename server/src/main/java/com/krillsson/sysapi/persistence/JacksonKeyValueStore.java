package com.krillsson.sysapi.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.boon.core.Function;
import org.boon.slumberdb.JsonKeyValueStore;
import org.boon.slumberdb.KeyValueStore;
import org.boon.slumberdb.base.BaseSimpleSerializationKeyValueStore;
import org.boon.slumberdb.serialization.ByteArrayToStringConverter;
import org.boon.slumberdb.serialization.StringToByteArrayConverter;

import java.io.IOException;

class JacksonKeyValueStore<V> extends BaseSimpleSerializationKeyValueStore<String, V> implements JsonKeyValueStore<String, V> {

    JacksonKeyValueStore(KeyValueStore<byte[], byte[]> store, Class<V> cls, ObjectMapper objectMapper) {
        super(store);
        this.keyObjectConverter = new ByteArrayToStringConverter();
        this.keyToByteArrayConverter = new StringToByteArrayConverter();
        this.valueObjectConverter = new ToJsonDecorator<>(objectMapper, cls);
        this.valueToByteArrayConverter = new FromJsonDecorator<>(objectMapper);
    }

    private static class FromJsonDecorator<T> implements Function<T, byte[]> {

        private final ObjectMapper objectMapper;

        private FromJsonDecorator(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public byte[] apply(T t) {
            try {
                byte[] bytes = objectMapper.writeValueAsBytes(t);
                return bytes;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class ToJsonDecorator<T> implements Function<byte[], T> {
        private final ObjectMapper objectMapper;
        private final Class<T> cls;

        private ToJsonDecorator(ObjectMapper objectMapper, Class<T> cls) {
            this.objectMapper = objectMapper;
            this.cls = cls;
        }


        @Override
        public T apply(byte[] bytes) {
            try {
                T t = objectMapper.readValue(bytes, cls);
                return t;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
