package de.wevsvirushackathon.coronareport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public final class TestUtil {

    private TestUtil() {

    }

    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }

    public static String toJson(final Object obj) throws JsonProcessingException {
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(obj);
    }

    public static <T> T fromJson(final String jsonString, final Class<T> clazz) throws IOException {
        return mapper.readValue(jsonString, clazz);
    }

}
