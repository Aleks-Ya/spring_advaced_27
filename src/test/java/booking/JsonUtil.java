package booking;

public class JsonUtil {

    public static String format(String singleQuotationMarkJson, String... args) {
        return String.format(singleQuotationMarkJson, args).replaceAll("'", "\"");
    }

    private JsonUtil() {
    }
}
