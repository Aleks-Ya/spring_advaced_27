package booking.web.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SeatHelper {
    private SeatHelper() {
    }

    public static List<Integer> parseSeatsString(String seats) {
        return Stream.of(seats.split(",")).map(Integer::valueOf).collect(Collectors.toList());
    }
}
