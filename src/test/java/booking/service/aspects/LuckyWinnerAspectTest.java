package booking.service.aspects;

import booking.BaseServiceTest;
import booking.domain.User;
import booking.service.aspects.mocks.LuckyWinnerAspectMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = AspectConfig.class)
@TestPropertySource(properties = {"lucky.enabled=true", "lucky.percentage=100"})
public class LuckyWinnerAspectTest extends BaseServiceTest {

    @Before
    public void initAspect() {
        LuckyWinnerAspectMock.cleanup();
    }

    @After
    public void cleanupAspect() {
        LuckyWinnerAspectMock.cleanup();
    }

    @Test
    public void testCalculateDiscount() {
        User user = to.createJohnBornToday();
        to.bookTicketToParty(user.getId(), "5,6");
        to.bookTicketToParty(user.getId(), "7,8");
        to.bookTicketToParty(user.getId(), "9,10");
        to.bookTicketToParty(user.getId(), "11,12");
        assertEquals(Collections.singletonList(user.getEmail()), LuckyWinnerAspectMock.getLuckyUsers());
    }
}
