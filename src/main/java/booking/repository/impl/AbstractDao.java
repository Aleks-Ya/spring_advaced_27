package booking.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
abstract class AbstractDao {

    @Autowired
    private SessionFactory sessionFactory;

    Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    Criteria createBlankCriteria(Class clazz) {
        return getCurrentSession().createCriteria(clazz);
    }
}
