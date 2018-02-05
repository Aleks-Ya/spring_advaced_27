package booking.repository.config;

import booking.domain.Account;
import booking.domain.Auditorium;
import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:db.properties")
public class DbSessionFactoryConfig {
    private final DataSource dataSource;
    private final String dialect;
    private final String showSql;
    private final String hbm2ddlAuto;
    private final String importFiles;

    @Autowired
    public DbSessionFactoryConfig(DataSource dataSource,
                                  @Value("${hibernate.dialect}") String dialect,
                                  @Value("${hibernate.show_sql}") String showSql,
                                  @Value("${hibernate.hbm2ddl.auto}") String hbm2ddlAuto,
                                  @Value("${hibernate.hbm2ddl.import_files}") String importFiles
    ) {
        this.dataSource = dataSource;
        this.dialect = dialect;
        this.showSql = showSql;
        this.hbm2ddlAuto = hbm2ddlAuto;
        this.importFiles = importFiles;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);

        // "no-import" is name of not-exists file. Can't use null or empty string because of big warnings in log.
        String importFilesValue = importFiles != null && !importFiles.isEmpty() ? importFiles : "no-import";
        properties.setProperty("hibernate.hbm2ddl.import_files", importFilesValue);

        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource);
        localSessionFactoryBean.setHibernateProperties(properties);
        localSessionFactoryBean.setAnnotatedClasses(Auditorium.class, User.class, Event.class,
                Ticket.class, Booking.class, Account.class);
        return localSessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }
}
