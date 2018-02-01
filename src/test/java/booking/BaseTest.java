package booking;

import org.junit.runner.RunWith;
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
}