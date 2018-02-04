package booking.service.aspects;

import booking.domain.Ticket;
import booking.domain.User;
import booking.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/11/2016
 * Time: 10:23 AM
 */
@Aspect
@Component
@PropertySource({"classpath:aspects/aspects.properties"})
public class LuckyWinnerAspect {
    private static final Logger LOG = LoggerFactory.getLogger(LuckyWinnerAspect.class);

    protected static final Set<String> luckyUsers = new HashSet<>();
    private final boolean luckyEnabled;
    private final int luckyPercentage;
    private final UserService userService;

    @Autowired
    public LuckyWinnerAspect(UserService userService,
                             @Value("${lucky.enabled}") boolean luckyEnabled,
                             @Value("${lucky.percentage}") int luckyPercentage
    ) {
        this.userService = userService;
        this.luckyEnabled = luckyEnabled;
        this.luckyPercentage = luckyPercentage;
        LOG.info("Properties: luckyEnabled={}, luckyPercentage={}", luckyEnabled, luckyPercentage);
    }

    public static List<String> getLuckyUsers() {
        return new ArrayList<>(luckyUsers);
    }

    @Pointcut(
            value = "(execution(* booking.service.BookingService.create(long, booking.domain.Ticket)) && args(userId, ticket))",
            argNames = "userId,ticket"
    )
    private void bookTicket(long userId, Ticket ticket) {
        // This method intended for declaring a @Pointcut
    }

    @Around(value = "bookTicket(userId, ticket)", argNames = "joinPoint,userId,ticket")
    public Object countBookTicketByName(ProceedingJoinPoint joinPoint, long userId, Ticket ticket) throws Throwable {
        final int randomInt = ThreadLocalRandom.current().nextInt(100 - luckyPercentage + 1);
        if (luckyEnabled && randomInt == 0) {
            Ticket luckyTicket = new Ticket(ticket.getEvent(), ticket.getDateTime(), ticket.getSeatsList(), 0.0);
            User user = userService.getById(userId);
            luckyUsers.add(user.getEmail());
            return joinPoint.proceed(new Object[]{userId, luckyTicket});
        } else {
            return joinPoint.proceed();
        }
    }
}
