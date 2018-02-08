package booking.web.controller;

import booking.BaseWebTest;
import booking.domain.Event;
import booking.domain.User;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static booking.web.controller.LoginControllerTest.ANONYMOUS_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {DiscountController.class})
public class DiscountControllerTest extends BaseWebTest {

    @Test
    public void getNonZeroDiscount() throws Exception {
        User user = testObjects.createJohnBornToday();
        Event event = testObjects.createParty();
        mvc.perform(get(DiscountController.ENDPOINT)
                .param("userId", String.valueOf(user.getId()))
                .param("eventId", String.valueOf(event.getId()))
        )
                .andExpect(status().isOk())
                .andExpect(content().string(ANONYMOUS_HEADER + "<h1>Discount</h1>\n<p>0.05</p>"));
    }

    @Test
    public void getZeroDiscount() throws Exception {
        User user = testObjects.createJohn();
        Event event = testObjects.createParty();
        mvc.perform(get(DiscountController.ENDPOINT)
                .param("userId", String.valueOf(user.getId()))
                .param("eventId", String.valueOf(event.getId()))
        )
                .andExpect(status().isOk())
                .andExpect(content().string(ANONYMOUS_HEADER + "<h1>Discount</h1>\n<p>0</p>"));
    }
}