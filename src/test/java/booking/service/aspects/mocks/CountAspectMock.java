package booking.service.aspects.mocks;

import booking.service.aspects.CounterAspect;

public class CountAspectMock extends CounterAspect {

    public static void cleanup() {
        accessByNameCounter.clear();
        getPriceByNameCounter.clear();
        bookByNameCounter.clear();
    }
}
