package booking.web.security.booking;

import booking.BaseWebSecurityTest;
import booking.web.controller.BookingController;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Check that an unauthorized user will be redirected to the login page.
 *
 * @author Aleksey Yablokov
 */
@ContextConfiguration(classes = BookingController.class)
public class BookingAnonymousTest extends BaseWebSecurityTest {

    @Test
    public void getBookedTickets() {
        assertRedirectToLoginPage(get(BookingController.ENDPOINT));
    }

    @Test
    public void getTicketById() {
        assertRedirectToLoginPage(get(BookingController.ENDPOINT + "/id/1"));
    }

    @Test
    public void bookTicket() {
        assertRedirectToLoginPage(post(BookingController.ENDPOINT));
    }

    @Test
    public void getTicketPrice() {
        assertRedirectToLoginPage(get(BookingController.ENDPOINT + "/price"));
    }

    @Test
    public void getTicketsForEvent() {
        assertRedirectToLoginPage(get(BookingController.ENDPOINT + "/tickets"));
    }

}