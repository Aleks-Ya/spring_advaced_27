package beans.daos.db;

import beans.daos.AbstractDAO;
import beans.daos.AuditoriumDAO;
import beans.models.Auditorium;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 20/2/16
 * Time: 4:35 PM
 */
@Repository(value = "auditoriumDAO")
public class AuditoriumDAOImpl extends AbstractDAO implements AuditoriumDAO {

    @Override
    @SuppressWarnings("unchecked")
    public List<Auditorium> getAll() {
        return ((List<Auditorium>) createBlankCriteria(Auditorium.class).list());
    }

    @Override
    public Auditorium getByName(String name) {
        return ((Auditorium) createBlankCriteria(Auditorium.class).add(Restrictions.eq("name", name)).uniqueResult());
    }

    @Override
    public Auditorium add(Auditorium auditorium) {
        Long id = (Long) getCurrentSession().save(auditorium);
        return auditorium.withId(id);
    }

    @Override
    public void delete(Auditorium auditorium) {
        getCurrentSession().delete(auditorium);
    }
}
