package booking;

import booking.service.TestObjects;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@TestPropertySource("classpath:db-test.properties")
public abstract class BaseTest {
    @Autowired
    protected TestObjects testObjects;

    @After
    public void clean() {
        testObjects.cleanup();
    }
}