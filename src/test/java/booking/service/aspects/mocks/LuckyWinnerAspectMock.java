package booking.service.aspects.mocks;

import booking.service.aspects.LuckyWinnerAspect;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 8:38 PM
 */
@Deprecated
public class LuckyWinnerAspectMock extends LuckyWinnerAspect {

    public LuckyWinnerAspectMock(
//            int luckyPercentage
    ) {
//        super(luckyPercentage);
        super(25);
    }

    public static void cleanup() {
        luckyUsers.clear();
    }
}
