package booking.service;

import booking.domain.Auditorium;

import java.util.Arrays;
import java.util.Collections;

public class TestObjects {
    public static Auditorium auditoriumBlueHall() {
        return new Auditorium(1, "Blue hall", 15, Arrays.asList(1, 2, 3, 4, 5));
    }

    public static Auditorium auditoriumRedHall() {
        return new Auditorium("Red hall", 8, Collections.singletonList(1));
    }
}
