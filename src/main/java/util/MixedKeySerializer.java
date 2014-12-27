package util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MixedKeySerializer extends JsonSerializer<MixedKey<?>>
{

    @Override
    public void serialize(MixedKey<?> tMixedKey, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(tMixedKey.getCls().getCanonicalName() + "|" + tMixedKey.getKey());
    }
}
