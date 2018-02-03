package booking;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Parent class for unit tests that don't use datasource.
 *
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BaseTest.Empty.class)
public abstract class BaseTest {

    /**
     * ContextConfiguration can't be without @Configuration.
     */
    @Configuration
    static class Empty {
    }
}