package booking.exception;

import booking.web.error.WebErrorHandler;

/**
 * {@link WebErrorHandler} shows {@link BookingException#userMessage} to user.
 */
public class BookingException extends RuntimeException {
    private final String userMessage;
    private final String header;

    BookingException(String header, String userMessage) {
        super(header + ": " + userMessage);
        this.header = header;
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getHeader() {
        return header;
    }
}
