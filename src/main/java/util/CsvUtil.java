package util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 21/2/16
 * Time: 12:09 PM
 */
public class CsvUtil {

    public static final String DELIMITER = ",";

    public static <T extends Number> List<T> fromCsvToList(String csv, Function<String, T> mapper) {
        return Arrays.asList(csv.split(DELIMITER)).stream().map(mapper:: apply).collect(Collectors.toList());
    }

    public static <T> String fromListToCsv(List<T> list) {
        return list.stream().map(Object:: toString).collect(Collectors.joining(DELIMITER));
    }
}
