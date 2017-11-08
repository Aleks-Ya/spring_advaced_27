package beans.daos.mocks;

import beans.daos.db.EventDAOImpl;
import beans.models.Event;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 2:41 PM
 */
public class EventDAOMock extends EventDAOImpl {

    private final List<Event> events;

    public EventDAOMock(List<Event> events) {
        this.events = events;
    }

    public void init() {
        cleanup();
        events.forEach(this :: create);
    }

    public void cleanup() {
        System.out.println("deleting ");
        System.out.println(getAll());
        getAll().forEach(this :: delete);
    }
}
