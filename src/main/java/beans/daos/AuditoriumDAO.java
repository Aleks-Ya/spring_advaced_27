package beans.daos;

import beans.models.Auditorium;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 11:09 AM
 */
public interface AuditoriumDAO {

    List<Auditorium> getAll();

    Auditorium getByName(String name);

    void delete(Auditorium auditorium);

    Auditorium add(Auditorium auditorium);
}
