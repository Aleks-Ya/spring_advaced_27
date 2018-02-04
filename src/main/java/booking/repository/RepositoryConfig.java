package booking.repository;

import booking.repository.config.DataSourceConfig;
import booking.repository.config.DbSessionFactoryConfig;
import booking.repository.config.PropertySourceConfig;
import booking.repository.impl.*;
import org.springframework.context.annotation.Import;

/**
 * @author Aleksey Yablokov
 */
@Import({DataSourceConfig.class, DbSessionFactoryConfig.class, PropertySourceConfig.class,
        AuditoriumDAOImpl.class, TicketDaoImpl.class, EventDAOImpl.class, UserDAOImpl.class, BookingDaoImpl.class})
public class RepositoryConfig {
}
