package beans.aspects;

import beans.models.User;
import beans.services.discount.BirthdayStrategy;
import beans.services.discount.TicketsStrategy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/11/2016
 * Time: 10:23 AM
 */
@Aspect
@Component
public class DiscountAspect {

    protected static final Map<String, Map<String, Integer>> discountPerUserCounter = new HashMap<>();

    static {
        discountPerUserCounter.put(BirthdayStrategy.class.getSimpleName(), new HashMap<>());
        discountPerUserCounter.put(TicketsStrategy.class.getSimpleName(), new HashMap<>());
    }

    @Pointcut("(execution(* beans.services.discount.DiscountStrategy.calculateDiscount(beans.models.User)) && args(user))")
    private void calculateDiscount(User user) {
    }

    @AfterReturning(pointcut = "calculateDiscount(user)", returning = "discount")
    public void countCalculateDiscount(JoinPoint joinPoint, User user, double discount) {
        if (Double.compare(discount, 0.0) > 0) {
            final Class<?> discountStrategy = joinPoint.getTarget().getClass();
            if (discountStrategy.isAssignableFrom(BirthdayStrategy.class)) {
                increaseCounter(BirthdayStrategy.class.getSimpleName(), user);
            } else if (discountStrategy.isAssignableFrom(TicketsStrategy.class)) {
                increaseCounter(TicketsStrategy.class.getSimpleName(), user);
            } else {
                System.out.println("Unknown discount strategy: [" + discountStrategy.getName() + "]");
            }
        }
    }

    private void increaseCounter(String cls, User user) {
        final Map<String, Integer> userDiscountMap = discountPerUserCounter.get(cls);
        userDiscountMap.put(user.getEmail(), userDiscountMap.getOrDefault(user.getEmail(), 0) + 1);
    }

    public static Map<String, Map<String, Integer>> getDiscountStatistics() {
        return Collections.unmodifiableMap(discountPerUserCounter);
    }

}
