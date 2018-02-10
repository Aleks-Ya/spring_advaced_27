package booking.web.controller;

import booking.exception.BookingExceptionFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SeatHelper {
    private SeatHelper() {
    }

    public static List<Integer> parseSeatsString(String seats) {
        return Stream.of(seats.split(",")).map(Integer::valueOf).collect(Collectors.toList());
    }

    public static void verifySeatList(List<Integer> seats) {
        if (Objects.isNull(seats) || seats.contains(null)) {
            throw BookingExceptionFactory.incorrectSeatsList(seats);
        }
    }
}
