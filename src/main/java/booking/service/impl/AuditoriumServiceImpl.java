package booking.service.impl;

import booking.domain.Auditorium;
import booking.repository.AuditoriumDao;
import booking.service.AuditoriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 11:14 AM
 */
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
    public Auditorium getByName(String auditoriumName) {
        return auditoriumDao.getByName(auditoriumName);
    }

    @Override
    public Auditorium getById(Long auditoriumId) {
        return auditoriumDao.getById(auditoriumId).orElse(null);
    }

    @Override
    public int getSeatsNumber(String auditoriumName) {
        return auditoriumDao.getByName(auditoriumName).getSeatsNumber();
    }

    @Override
    public List<Integer> getVipSeats(String auditoriumName) {
        return auditoriumDao.getByName(auditoriumName).getVipSeatsList();
    }

    @Override
    public Auditorium create(Auditorium auditorium) {
        return auditoriumDao.add(auditorium);
    }

    @Override
    public void delete(Long auditoriumId) {
        auditoriumDao.delete(auditoriumId);
    }
}
