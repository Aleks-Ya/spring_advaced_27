package booking.service.impl;

import booking.domain.Account;
import booking.domain.User;
import booking.exception.BookingExceptionFactory;
import booking.repository.UserDao;
import booking.service.AccountService;
import booking.service.UserService;
import booking.web.security.ExtendedUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static booking.exception.BookingExceptionFactory.notFoundById;
import static booking.exception.BookingExceptionFactory.userAlreadyExistWithEmail;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, @Lazy AccountService accountService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
    }

    public User register(User user) {
        Optional<User> userOpt = userDao.getByEmail(user.getEmail());
        if (userOpt.isPresent()) {
            throw userAlreadyExistWithEmail(user.getEmail());
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        User newUser = userDao.create(new User(user.getId(), user.getEmail(), user.getName(),
                user.getBirthday(), encodedPassword, user.getRoles()));
        accountService.create(new Account(newUser, BigDecimal.ZERO));
        return newUser;
    }

    public void delete(User user) {
        userDao.delete(user.getId());
    }

    public User getById(long userId) {
        return userDao.getById(userId).orElseThrow(() -> notFoundById(User.class, userId));
    }

    public User getByEmail(String email) {
        return userDao.getByEmail(email).orElseThrow(() -> BookingExceptionFactory.userNotFoundByEmail(email));
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
        return userDao.getByEmail(email).orElse(null);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

}
