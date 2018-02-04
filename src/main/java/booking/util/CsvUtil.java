package booking.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvUtil {

    private CsvUtil() {
    }

    @SuppressWarnings("WeakerAccess")
    public static final String DELIMITER = ",";

    public static <T extends Number> List<T> fromCsvToList(String csv, Function<String, T> mapper) {
        return Arrays.stream(csv.split(DELIMITER)).map(mapper).collect(Collectors.toList());
    }

    public static <T> String fromListToCsv(List<T> list) {
        return list.stream().map(Object::toString).collect(Collectors.joining(DELIMITER));
    }
}
