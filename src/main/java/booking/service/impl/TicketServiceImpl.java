package booking.service.impl;

import booking.domain.Auditorium;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.repository.TicketDao;
import booking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Service
@PropertySource({"classpath:strategies/booking.properties"})
@Transactional
public class TicketServiceImpl implements TicketService {

    private final int minSeatNumber;
    private final double vipSeatPriceMultiplier;
    private final double highRatedPriceMultiplier;
    private final double defaultRateMultiplier;
    private final EventService eventService;
    private final AuditoriumService auditoriumService;
    private final UserService userService;
    private final TicketDao ticketDao;
    private final DiscountService discountService;

    @Autowired
    public TicketServiceImpl(@Qualifier("eventServiceImpl") EventService eventService,
                             @Qualifier("auditoriumServiceImpl") AuditoriumService auditoriumService,
                             @Qualifier("userServiceImpl") UserService userService,
                             @Qualifier("discountServiceImpl") DiscountService discountService,
                             TicketDao ticketDao,
                             @Value("${min.seat.number}") int minSeatNumber,
                             @Value("${vip.seat.price.multiplier}") double vipSeatPriceMultiplier,
                             @Value("${high.rate.price.multiplier}") double highRatedPriceMultiplier,
                             @Value("${def.rate.price.multiplier}") double defaultRateMultiplier) {
        this.eventService = eventService;
        this.auditoriumService = auditoriumService;
        this.userService = userService;
        this.ticketDao = ticketDao;
        this.discountService = discountService;
        this.minSeatNumber = minSeatNumber;
        this.vipSeatPriceMultiplier = vipSeatPriceMultiplier;
        this.highRatedPriceMultiplier = highRatedPriceMultiplier;
        this.defaultRateMultiplier = defaultRateMultiplier;
    }

    @Override
    public Ticket create(Ticket ticket) {
        return ticketDao.create(ticket);
    }

    @Override
    public List<Ticket> getTicketsForEvent(String eventName, Long auditoriumId, LocalDateTime date) {
        final Auditorium auditorium = auditoriumService.getById(auditoriumId);
        final Event foundEvent = eventService.getEvent(eventName, auditorium, date);
        return ticketDao.getTickets(foundEvent);
    }

    @Override
    public List<Ticket> getAll() {
        return ticketDao.getAll();
    }

    @Override
    public Ticket getTicketById(Long ticketId) {
        return ticketDao.getTicketById(ticketId).orElse(null);
    }

    @Override
    public void delete(long ticketId) {
        ticketDao.delete(ticketId);
    }
}
