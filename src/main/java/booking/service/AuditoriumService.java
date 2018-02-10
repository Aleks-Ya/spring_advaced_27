package booking.service;

import booking.domain.Auditorium;

import java.util.List;
import java.util.Optional;

public interface AuditoriumService {

    List<Auditorium> getAll();

    Optional<Auditorium> getById(Long auditoriumId);

    Optional<Integer> getSeatsNumber(long auditoriumId);

    List<Integer> getVipSeats(long auditoriumId);

    Auditorium create(Auditorium auditorium);

    void delete(Long auditoriumId);
}
