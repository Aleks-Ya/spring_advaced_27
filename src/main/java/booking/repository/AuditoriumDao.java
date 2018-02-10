package booking.repository;

import booking.domain.Auditorium;

import java.util.List;
import java.util.Optional;

public interface AuditoriumDao {

    List<Auditorium> getAll();

    Optional<Auditorium> getById(Long auditoriumId);

    void delete(Long auditoriumId);

    Auditorium create(Auditorium auditorium);
}
