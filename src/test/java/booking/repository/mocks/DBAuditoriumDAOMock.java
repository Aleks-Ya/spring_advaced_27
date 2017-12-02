package booking.repository.mocks;

import booking.domain.Auditorium;
import booking.repository.impl.AuditoriumDAOImpl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 1:27 PM
 */
public class DBAuditoriumDAOMock extends AuditoriumDAOImpl {

    private final List<Auditorium> auditoriums;

    public DBAuditoriumDAOMock(List<Auditorium> auditoriums) {
        this.auditoriums = auditoriums;
    }

    public void init() {
        cleanup();
        auditoriums.forEach(this::add);
    }

    public void cleanup() {
        getAll().stream().map(Auditorium::getId).forEach(this::delete);
    }
}
