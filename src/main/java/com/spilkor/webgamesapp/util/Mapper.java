package com.spilkor.webgamesapp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Mapper {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <CLASS> CLASS readValue(String value, Class<CLASS> clazz) throws JsonProcessingException {
        return mapper.readValue(value, clazz);
    }

    public static String writeValueAsString(Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }

}
