package booking.domain.config.db;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 20/2/16
 * Time: 4:00 PM
 */
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:db.properties")
public class DbSessionFactoryConfig {
    private final DataSource dataSource;
    private final String dialect;
    private final String showSql;
    private final String hbm2ddlAuto;

    @Autowired
    public DbSessionFactoryConfig(@Qualifier("dataSource") DataSource dataSource,
                                  @Value("${hibernate.dialect}") String dialect,
                                  @Value("${hibernate.show_sql}") String showSql,
                                  @Value("${hibernate.hbm2ddl.auto}") String hbm2ddlAuto) {
        this.dataSource = dataSource;
        this.dialect = dialect;
        this.showSql = showSql;
        this.hbm2ddlAuto = hbm2ddlAuto;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource);
        localSessionFactoryBean.setHibernateProperties(new Properties() {{
            setProperty("hibernate.dialect", dialect);
            setProperty("hibernate.show_sql", showSql);
            setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        }});
        localSessionFactoryBean.setMappingResources("/mappings/auditorium.hbm.xml", "/mappings/event.hbm.xml",
                "/mappings/ticket.hbm.xml", "/mappings/user.hbm.xml",
                "/mappings/booking.hbm.xml");
        return localSessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }
}
