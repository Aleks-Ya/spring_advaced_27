package booking.web.error;

import booking.exception.BookingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Redirect to the error page if any exception is thrown.
 * <p>
 * {@link BookingException} is shown with detailed information
 * ({@link BookingException#header} and {@link BookingException#userMessage}).
 * Other exception is shown without details.
 */
@ControllerAdvice("booking.web.controller")
public class WebErrorHandler {
    private static final String HEADER_ATTR = "header";
    private static final String ERROR_MESSAGE_ATTR = "errorMessage";
    private static final String ERROR_FTL = "error/error";

    @SuppressWarnings("unused")
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Exception e) {
        String header = "An error occurred";
        String message = "Sorry, an error happened.";
        if (e instanceof BookingException) {
            BookingException be = (BookingException) e;
            header = be.getHeader();
            message = be.getUserMessage();
        }
        Map<String, String> model = new HashMap<>();
        model.put(HEADER_ATTR, header);
        model.put(ERROR_MESSAGE_ATTR, message);
        return new ModelAndView(ERROR_FTL, model); //TODO pass data without "model" everywhere (like here)
    }
}
