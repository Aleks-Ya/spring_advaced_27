package booking.web.controller;

import booking.BaseWebTest;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static booking.util.ResourceUtil.resourceToString;
import static booking.web.controller.LoginControllerTest.ANONYMOUS_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {RootController.class})
public class RootControllerTest extends BaseWebTest {
    public static final String NAVIGATOR = "<p><a href='/'>To home page</a></p>\n";

    @Test
    public void rootPage() throws Exception {
        String expBody = ANONYMOUS_HEADER
                + resourceToString("RootControllerTest_rootPage.html", LoginController.class);
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(expBody));
    }
}