package beans.daos.inmemory;

import beans.daos.AuditoriumDAO;
import beans.models.Auditorium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/4/2016
 * Time: 11:09 AM
 */
@Repository(value = "inMemoryAuditoriumDAO")
public class InMemoryAuditoriumDAO implements AuditoriumDAO {

    private final List<Auditorium> db;

    @Autowired
    public InMemoryAuditoriumDAO(@Value("#{auditoriumList}") List<Auditorium> db) {
        this.db = db;
    }

    @Override
    public List<Auditorium> getAll() {
        return db.stream().collect(Collectors.toList());
    }

    @Override
    public Auditorium getByName(String auditoriumName) {
        return db.stream().filter(auditorium -> Objects.equals(auditorium.getName(), auditoriumName)).findFirst()
                 .orElseThrow(() -> new IllegalArgumentException(
                         "Auditorium with name: [" + auditoriumName + "] does not exist"));
    }

    @Override
    public void delete(Auditorium auditorium) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Auditorium add(Auditorium auditorium) {
        throw new UnsupportedOperationException("not implemented");
    }
}
