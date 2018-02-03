package booking;

import booking.web.security.SecurityConfig;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * @author Aleksey Yablokov
 */
@WebAppConfiguration
@ContextConfiguration(classes = {SecurityConfig.class,})
public abstract class BaseWebSecurityTest extends BaseWebTest {
    @Autowired
    protected WebApplicationContext wc;
    protected MockMvc mvc;

    @Before
    @Override
    public void initMockMvc() {
        mvc = MockMvcBuilders
                .webAppContextSetup(wc)
                .apply(springSecurity())
                .build();
    }
}