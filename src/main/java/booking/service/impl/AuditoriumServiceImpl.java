package booking.service.impl;

import booking.domain.Auditorium;
import booking.repository.AuditoriumDao;
import booking.service.AuditoriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static booking.exception.BookingExceptionFactory.notFoundById;
import static java.util.Collections.emptyList;

@Service
public class AuditoriumServiceImpl implements AuditoriumService {

    private final AuditoriumDao auditoriumDao;

    @Autowired
    public AuditoriumServiceImpl(AuditoriumDao auditoriumDao) {
        this.auditoriumDao = auditoriumDao;
    }

    @Override
    public List<Auditorium> getAll() {
        return auditoriumDao.getAll();
    }

    @Override
    public Auditorium getById(Long auditoriumId) {
        return auditoriumDao.getById(auditoriumId).orElseThrow(() -> notFoundById(Auditorium.class, auditoriumId));
    }

    @Override
    public Optional<Integer> getSeatsNumber(long auditoriumId) {
        return auditoriumDao.getById(auditoriumId).map(Auditorium::getSeatsNumber);
    }

    @Override
    public List<Integer> getVipSeats(long auditoriumId) {
        Optional<Auditorium> auditoriumOpt = auditoriumDao.getById(auditoriumId);
        if (auditoriumOpt.isPresent()) {
            return auditoriumOpt.get().getVipSeatsList();
        } else {
            return emptyList();
        }
    }

    @Override
    public Auditorium create(Auditorium auditorium) {
        return auditoriumDao.create(auditorium);
    }

    @Override
    public void delete(Long auditoriumId) {
        auditoriumDao.delete(auditoriumId);
    }
}
