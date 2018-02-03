package booking;

import booking.web.config.FreeMarkerConfig;
import booking.web.config.MvcConfig;
import booking.web.error.AdviceErrorHandler;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Parent class for unit tests that use repository, service and web layers.
 *
 * @author Aleksey Yablokov
 */
@WebAppConfiguration
@ContextConfiguration(classes = {FreeMarkerConfig.class, AdviceErrorHandler.class, MvcConfig.class})
public abstract class BaseWebTest extends BaseServiceTest {
    @Autowired
    protected WebApplicationContext wc;
    protected MockMvc mvc;

    @Before
    public void initMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(wc).build();
    }
}