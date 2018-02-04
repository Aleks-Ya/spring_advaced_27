package booking.service.aspects.mocks;

import booking.service.UserService;
import booking.service.aspects.LuckyWinnerAspect;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 8:38 PM
 */
public class LuckyWinnerAspectMock extends LuckyWinnerAspect {

    public LuckyWinnerAspectMock(int luckyPercentage, UserService userService) {
        super(userService, true, luckyPercentage);
    }

    public static void cleanup() {
        luckyUsers.clear();
    }
}
