package booking.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 20/2/16
 * Time: 6:50 PM
 */
@Transactional
abstract class AbstractDAO {

    @Autowired
    private SessionFactory sessionFactory;

    Session getCurrentSession() {return sessionFactory.getCurrentSession();}

    Criteria createBlankCriteria(Class clazz) {return getCurrentSession().createCriteria(clazz);}
}
