package booking.beans.daos;

import booking.beans.models.Auditorium;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 11:09 AM
 */
public interface AuditoriumDAO {

    List<Auditorium> getAll();

    Auditorium getByName(String auditoriumName);

    Optional<Auditorium> getById(Long auditoriumId);

    void delete(Long auditoriumId);

    Auditorium add(Auditorium auditorium);
}
