package booking;

import booking.web.config.FreeMarkerConfig;
import booking.web.config.MvcConfig;
import booking.web.error.AdviceErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Aleksey Yablokov
 */
@WebAppConfiguration
@ContextConfiguration(classes = {FreeMarkerConfig.class, AdviceErrorHandler.class, MvcConfig.class})
public abstract class BaseWebTest extends BaseTest {
    @Autowired
    protected WebApplicationContext wc;
    protected MockMvc mvc;
}