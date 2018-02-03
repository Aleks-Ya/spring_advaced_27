package booking.web.security;

import booking.BaseWebSecurityTest;
import booking.web.controller.BookingController;
import booking.web.controller.LoginController;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private void assertRedirectToLoginPage(MockHttpServletRequestBuilder builder) {
        try {
            MvcResult mvcResult = mvc.perform(builder)
                    .andExpect(status().is3xxRedirection())
                    .andReturn();
            String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();
            assertThat(redirectedUrl, endsWith(LoginController.LOGIN_ENDPOINT));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}