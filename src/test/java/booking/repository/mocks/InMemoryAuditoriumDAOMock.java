package booking.repository.mocks;

import booking.domain.Auditorium;
import booking.repository.inmemory.InMemoryAuditoriumDAO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 1:27 PM
 */
public class InMemoryAuditoriumDAOMock extends InMemoryAuditoriumDAO {

    public InMemoryAuditoriumDAOMock(List<Auditorium> auditoriums) {
        super(auditoriums);
    }
}
