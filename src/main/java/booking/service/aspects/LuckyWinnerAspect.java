package booking.service.aspects;

import booking.domain.Ticket;
import booking.domain.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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

    protected static final Set<String> luckyUsers = new HashSet<>();
    private final int luckyPercentage;

//    TODO
//    @Autowired
    public LuckyWinnerAspect(@Value("${lucky.percentage}") int luckyPercentage) {
//        this.luckyPercentage = luckyPercentage;
        this.luckyPercentage = luckyPercentage;
    }

    public static List<String> getLuckyUsers() {
        return new ArrayList<>(luckyUsers);
    }

    @Pointcut(value = "(execution(* booking.service.BookingService.create(booking.domain.User, booking.domain.Ticket)) && args(user, ticket))",
            argNames = "user,ticket")
    private void bookTicket(User user, Ticket ticket) {
        // This method intended for declaring a @Pointcut
    }

    @Around(value = "bookTicket(user, ticket)", argNames = "joinPoint,user,ticket")
    public void countBookTicketByName(ProceedingJoinPoint joinPoint, User user, Ticket ticket) throws Throwable {
        final int randomInt = ThreadLocalRandom.current().nextInt(100 - luckyPercentage + 1);
        if (randomInt == 0) {
            Ticket luckyTicket = new Ticket(ticket.getEvent(), ticket.getDateTime(), ticket.getSeatsList(), ticket.getUser(), 0.0);
            luckyUsers.add(user.getEmail());
            joinPoint.proceed(new Object[]{user, luckyTicket});
        } else {
            joinPoint.proceed();
        }
    }
}
