package booking.service.impl;

import booking.domain.Auditorium;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.repository.TicketDao;
import booking.service.AuditoriumService;
import booking.service.EventService;
import booking.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final EventService eventService;
    private final AuditoriumService auditoriumService;
    private final TicketDao ticketDao;

    @Autowired
    public TicketServiceImpl(@Qualifier("eventServiceImpl") EventService eventService,
                             @Qualifier("auditoriumServiceImpl") AuditoriumService auditoriumService,
                             TicketDao ticketDao) {
        this.eventService = eventService;
        this.auditoriumService = auditoriumService;
        this.ticketDao = ticketDao;
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
