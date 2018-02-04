package booking.web.security.booking;

import booking.BaseWebSecurityTest;
import booking.web.controller.BookingController;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Check that an unauthorized user will be redirected to the login page.
 */
@ContextConfiguration(classes = BookingController.class)
public class BookingAnonymousTest extends BaseWebSecurityTest {

    @Test
    public void getBookedTickets() {
        assertRedirectToLoginPage(get(BookingController.ROOT_ENDPOINT));
    }

    @Test
    public void getTicketById() {
        assertRedirectToLoginPage(get(BookingController.ROOT_ENDPOINT + "/id/1"));
    }

    @Test
    public void bookTicket() {
        assertRedirectToLoginPage(post(BookingController.ROOT_ENDPOINT));
    }

    @Test
    public void getTicketPrice() {
        assertRedirectToLoginPage(get(BookingController.PRICE_ENDPOINT));
    }

    @Test
    public void getTicketsForEvent() {
        assertRedirectToLoginPage(get(BookingController.TICKETS_ENDPOINT));
    }

}