package booking.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private JsonUtil() {
    }

    public static String format(String singleQuotationMarkJson, String... args) {
        return String.format(singleQuotationMarkJson, args).replaceAll("'", "\"");
    }

    public static <T> T readValue(byte[] src, Class<T> valueType) throws IOException {
        return objectMapper.readValue(src, valueType);
    }

    public static <T> T readValue(byte[] src, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(src, typeReference);
    }
}
