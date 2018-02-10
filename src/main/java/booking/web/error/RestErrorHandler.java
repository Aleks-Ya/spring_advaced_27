package booking.web.error;

import booking.exception.BookingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.lang.String.format;

/**
 * Make error response for REST controllers.
 */
@ControllerAdvice("booking.web.rest")
public class RestErrorHandler {

    @SuppressWarnings("unused")
    @ExceptionHandler(Exception.class)
    ResponseEntity<String> handle(Exception e) {
        String message = e.getMessage();
        if (e instanceof BookingException) {
            BookingException be = (BookingException) e;
            message = be.getUserMessage();
        }
        String body = format("{\"error\": \"%s\"}", message);
        return new ResponseEntity<>(body, HttpStatus.valueOf(400));
    }
}
