package booking.exception;

import booking.domain.Account;
import booking.domain.Auditorium;
import booking.domain.Event;
import booking.domain.User;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.String.format;

/**
 * Convenient and uniform creating {@link BookingException}.
 */
public class BookingExceptionFactory {

    private BookingExceptionFactory() {
    }

    public static BookingException notFoundById(Class<?> clazz, Long id) {
        String name = clazz.getSimpleName();
        return new BookingException(name + " is not found", name + " is not found by id " + id);
    }

    public static BookingException accountNotFoundByUserId(Long userId) {
        return new BookingException("Account is not found", "Account is not found by User id " + userId);
    }

    public static BookingException userNotFoundByEmail(String email) {
        return new BookingException("User is not found", "User is not found by email " + email);
    }

    public static BookingException userAlreadyExistWithEmail(String email) {
        return new BookingException("User already exists", format("User with e-mail %s already exist.", email));
    }

    public static BookingException userAlreadyHasAccount(User user) {
        return new BookingException("User already has account", format("User %s already has account", user.getEmail()));
    }

    public static BookingException notEnoughMoneyForBooking(Event event, String seats, Account account, BigDecimal price) {
        String message = format("Not enough money to book seats %s for event '%s'. Needed $%s, available $%s.",
                seats, event.getName(), price, account.getAmount());
        return new BookingException("Not enough money", message);
    }

    public static BookingException seatsAlreadyBooked(Event event, String seats) {
        String message = format("Can't book seats %s on '%s' because they are already booked.",
                seats, event.getName());
        return new BookingException("Seats are booked already", message);
    }

    public static BookingException incorrect(Class<?> clazz, Object object) {
        String name = clazz.getSimpleName();
        return incorrect(name, object);
    }

    public static BookingException incorrect(String name, Object object) {
        String message = format("%s '%s' is incorrect.", name, object);
        return new BookingException("Incorrect " + name.toLowerCase(), message);
    }


    public static BookingException incorrectSeatsList(List<Integer> seats) {
        String message = format("Seat list '%s' is incorrect.", seats);
        return new BookingException("Incorrect seat", message);
    }

    public static BookingException seatOutOfRange(int seat, Auditorium auditorium) {
        String message = String.format("Seat: [%s] is incorrect. Auditorium: [%s] has [%s] seats",
                seat, auditorium.getName(), auditorium.getSeatsNumber());
        return new BookingException("Incorrect seat", message);
    }
}
