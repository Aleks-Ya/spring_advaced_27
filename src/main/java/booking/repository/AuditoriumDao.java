package booking.repository;

import booking.domain.Auditorium;

import java.util.List;
import java.util.Optional;

public interface AuditoriumDao {

    List<Auditorium> getAll();

    Auditorium getByName(String auditoriumName);

    Optional<Auditorium> getById(Long auditoriumId);

    void delete(Long auditoriumId);

    Auditorium add(Auditorium auditorium);
}
