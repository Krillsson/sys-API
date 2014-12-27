package util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;


public class MixedKeyDeserializer extends JsonDeserializer<MixedKey<?>>
{

    @Override
    public MixedKey<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        try {
            return new MixedKey(Class.forName(node.get("cls").asText()), node.get("key").asText());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
