package booking.repository;

import booking.repository.config.DataSourceConfig;
import booking.repository.config.DbSessionFactoryConfig;
import booking.repository.config.PropertySourceConfig;
import booking.repository.impl.AuditoriumDAOImpl;
import booking.repository.impl.BookingDAOImpl;
import booking.repository.impl.EventDAOImpl;
import booking.repository.impl.UserDAOImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Aleksey Yablokov
 */
@Configuration
@Import({DataSourceConfig.class, DbSessionFactoryConfig.class, PropertySourceConfig.class,
        AuditoriumDAOImpl.class, BookingDAOImpl.class, EventDAOImpl.class, UserDAOImpl.class})
public class RepositoryConfig {
}
