package booking.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public final class ResourceUtil {

    private ResourceUtil() {
    }

    public static String resourceToString(String resource, Class<?> clazz) {
        try {
            URL url = clazz.getResource(resource);
            Path path = Paths.get(url.getFile());
            return Files.readAllLines(path).stream().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
