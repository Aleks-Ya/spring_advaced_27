package booking.service.aspects.mocks;

import booking.service.UserService;
import booking.service.aspects.LuckyWinnerAspect;

public class LuckyWinnerAspectMock extends LuckyWinnerAspect {

    public LuckyWinnerAspectMock(int luckyPercentage, UserService userService) {
        super(userService, true, luckyPercentage);
    }

    public static void cleanup() {
        luckyUsers.clear();
    }
}
