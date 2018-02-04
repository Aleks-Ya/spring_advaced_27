package booking.web.controller;

import booking.BaseWebTest;
import booking.domain.Account;
import booking.domain.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

import static booking.util.ResourceUtil.resourceToString;
import static booking.web.controller.LoginControllerTest.ANONYMOUS_HEADER;
import static booking.web.controller.RootControllerTest.NAVIGATOR;
import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = AccountController.class)
public class AccountControllerTest extends BaseWebTest {

    @Test
    public void refill() throws Exception {
        Account account = testObjects.createAccount();
        User user = account.getUser();
        BigDecimal refillingAmount = BigDecimal.valueOf(300.5);

        mvc.perform(post(AccountController.ROOT_ENDPOINT)
                .param("userId", String.valueOf(user.getId()))
                .param("amount", refillingAmount.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andExpect(status().isOk())
                .andExpect(content().string(format(ANONYMOUS_HEADER + NAVIGATOR +
                                "<h1>Account is refilled</h1>\n" +
                                "<p>User: %s</p>\n" +
                                "<p>Amount before: 10,000</p>\n" +
                                "<p>Amount after: 10,300.5</p>\n" +
                                "<p>Amount: 300.5</p>",
                        user.getName()
                )));
    }

    @Test
    public void getRefillingPage() throws Exception {
        User user = testObjects.createCurrentUser();
        String body = resourceToString("AccountControllerTest_getRefillingPage.html", AccountController.class);
        mvc.perform(get(AccountController.ROOT_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().string(format(body, user.getName(), user.getEmail(), user.getName(), user.getId())));
    }

}