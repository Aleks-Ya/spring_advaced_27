package booking.service.impl;

import booking.domain.User;
import booking.repository.UserDao;
import booking.service.UserService;
import booking.web.security.ExtendedUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, @Lazy PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        return userDao.create(new User(user.getId(), user.getEmail(), user.getName(),
                user.getBirthday(), encodedPassword, user.getRoles()));
    }

    public void delete(User user) {
        userDao.delete(user.getId());
    }

    public User getById(long id) {
        return userDao.getById(id);
    }

    public User getByEmail(String email) {
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
