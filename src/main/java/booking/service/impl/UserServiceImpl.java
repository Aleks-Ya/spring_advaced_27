package booking.service.impl;

import booking.domain.User;
import booking.repository.UserDao;
import booking.service.UserService;
import booking.web.security.ExtendedUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public User register(User user) {
        return userDao.create(user);
    }

    public void delete(User user) {
        userDao.delete(user);
    }

    public User getById(long id) {
        return userDao.getById(id);
    }

    public User getUserByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principalObj = auth.getPrincipal();
        if (!(principalObj instanceof ExtendedUserDetails)) {
            return null;
        }
        ExtendedUserDetails principal = (ExtendedUserDetails) principalObj;
        String email = principal.getEmail();
        return userDao.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

}
