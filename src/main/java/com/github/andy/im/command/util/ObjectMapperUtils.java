package com.github.andy.im.command.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class ObjectMapperUtils {

    public static final com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();;

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) ;
    }

    public static <T> String toJSON(T obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJSON(String json, Class<T> valueType) {
        try {
            return mapper.readValue(json, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final <T> T value(Object rawValue, Class<T> type) {
        return mapper.convertValue(rawValue, type);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T fromJSON(String json, Class<? extends Collection> collectionType,
                                 Class<?> valueType) {
        if ((json == null) || (json.length() == 0)) {
            try {
                return (T) collectionType.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return (T) mapper.readValue(json, TypeFactory.defaultInstance()
                    .constructCollectionType(collectionType, valueType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T fromJSON(String json, Class<? extends Map> mapType, Class<?> keyType,
                                 Class<?> valueType) {
        if ((json == null) || (json.length() == 0)) {
            try {
                return (T) mapType.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return (T) mapper.readValue(json,
                    TypeFactory.defaultInstance().constructMapType(mapType, keyType, valueType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String,Object> fromJSONToMap(String json) {
        return fromJSON(json,Map.class,String.class,Object.class);
    }

    public static com.fasterxml.jackson.databind.JsonNode toJsonNode(String data) throws IOException {
        return mapper.readTree(data);
    }


    public static ObjectNode createObjectNode(){
        return mapper.createObjectNode();
    }

}
