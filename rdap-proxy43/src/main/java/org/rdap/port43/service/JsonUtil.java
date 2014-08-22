package org.rdap.port43.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.rdap.port43.service.format.ResponseFormater;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author jiashuo
 * 
 */
public class JsonUtil {
    public static void main(String[] args) throws JsonParseException,
            JsonMappingException, IOException {
        String json =
                "{\"address\":[{\"a\":\"b\"},{\"c\":\"d\"}],\"name\":\"haha\",\"id\":1,\"email\":\"email\"}";
        Map convertToMap = convertToMap(json);
        System.err.println(convertToMap);
        System.err.println(ResponseFormater.format(convertToMap));
    }

    public static String toJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toJsonWithPrettyFormat(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Map convertToMap(String jsonStr) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map result = null;
        try {
            result = objectMapper.readValue(jsonStr, LinkedHashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

class JcardDeserializer extends JsonDeserializer<Map<String, Object>> {
    private ObjectMapper mapper; // ObjectMapper without special map
                                 // deserializer

    public JcardDeserializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Map<String, Object> deserialize(JsonParser jp,
            DeserializationContext ctxt) throws IOException,
            JsonProcessingException {
        ObjectCodec codec = jp.getCodec();
        JsonNode node = codec.readTree(jp);
        // if (!"".equals(node.getTextValue())) {
        // return mapper.readValue(node,
        // new TypeReference<Map<String, Object>>() {
        // });
        // }
        return null; // Node was an empty string
    }
}
