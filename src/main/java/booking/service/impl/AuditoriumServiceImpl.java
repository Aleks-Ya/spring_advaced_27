package booking.service.impl;

import booking.domain.Auditorium;
import booking.repository.AuditoriumDAO;
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

    private final AuditoriumDAO auditoriumDAO;

    @Autowired
    public AuditoriumServiceImpl(AuditoriumDAO auditoriumDAO) {
        this.auditoriumDAO = auditoriumDAO;
    }

    @Override
    public List<Auditorium> getAll() {
        return auditoriumDAO.getAll();
    }

    @Override
    public Auditorium getByName(String auditoriumName) {
        return auditoriumDAO.getByName(auditoriumName);
    }

    @Override
    public Auditorium getById(Long auditoriumId) {
        return auditoriumDAO.getById(auditoriumId).orElse(null);
    }

    @Override
    public int getSeatsNumber(String auditoriumName) {
        return auditoriumDAO.getByName(auditoriumName).getSeatsNumber();
    }

    @Override
    public List<Integer> getVipSeats(String auditoriumName) {
        return auditoriumDAO.getByName(auditoriumName).getVipSeatsList();
    }

    @Override
    public Auditorium create(Auditorium auditorium) {
        return auditoriumDAO.add(auditorium);
    }

    @Override
    public void delete(Long auditoriumId) {
        auditoriumDAO.delete(auditoriumId);
    }
}
