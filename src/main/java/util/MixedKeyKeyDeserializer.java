package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

public class MixedKeyKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String[] arr = s.split("|");
        try {
            return new MixedKey<>(Class.forName(arr[0]), arr[1]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
