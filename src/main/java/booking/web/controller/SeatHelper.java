package booking.web.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SeatHelper {
    private SeatHelper() {
    }

    static List<Integer> parseSeatsString(String seats) {
        return Stream.of(seats.split(",")).map(Integer::valueOf).collect(Collectors.toList());
    }
}
