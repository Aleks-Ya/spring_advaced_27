package booking.web.controller;

import booking.BaseWebTest;
import booking.util.ResourceUtil;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static booking.web.controller.LoginControllerTest.ANONYMOUS_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@ContextConfiguration(classes = {RootController.class})
public class RootControllerTest extends BaseWebTest {

    @Test
    public void rootPage() throws Exception {
        String expBody = ANONYMOUS_HEADER + ResourceUtil.resourceToString("root.html", LoginController.class);
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(expBody));
    }
}