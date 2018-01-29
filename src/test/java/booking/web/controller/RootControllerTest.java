package booking.web.controller;

import booking.repository.config.DataSourceConfig;
import booking.repository.config.DbSessionFactoryConfig;
import booking.repository.config.TestUserServiceConfig;
import booking.util.ResourceUtil;
import booking.web.config.FreeMarkerConfig;
import booking.web.config.MvcConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static booking.web.controller.LoginControllerTest.ANONYMOUS_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, FreeMarkerConfig.class, RootController.class,
        DataSourceConfig.class, DbSessionFactoryConfig.class, TestUserServiceConfig.class
})
public class RootControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void rootPage() throws Exception {
        String expBody = ANONYMOUS_HEADER + ResourceUtil.resourceToString("root.html", LoginController.class);
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(expBody));
    }
}