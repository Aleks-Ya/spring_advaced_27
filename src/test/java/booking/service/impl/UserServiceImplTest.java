package booking.service.impl;

import booking.BaseServiceTest;
import booking.domain.User;
import booking.exception.BookingException;
import org.junit.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class UserServiceImplTest extends BaseServiceTest {

    @Test
    public void testRegister() {
        String email = UUID.randomUUID().toString();
        String name = UUID.randomUUID().toString();
        User user = new User(email, name, LocalDate.now(), "mypass", "admin,user");
        User expUser = userService.register(user);
        User actUserByEmail = userService.getByEmail(email);
        assertEquals("User should be the same", expUser, actUserByEmail);
    }

    @Test(expected = BookingException.class)
    public void testRegisterSameUser() {
        User user = to.createJohn();
        userService.register(user);
    }

    @Test
    public void testDelete() {
        User user = to.createJohn();
        assertThat(userService.getById(user.getId()), equalTo(user));
        userService.delete(user);
        assertThat(userService.getAll(), emptyIterable());
    }

    @Test
    public void testGetUserByEmail() {
        User user = to.createJohn();
        User foundUser = userService.getByEmail(user.getEmail());
        assertEquals("User should match", user, foundUser);
    }

    @Test(expected = BookingException.class)
    public void testGetUserByEmail_NotExists() {
        userService.getByEmail("not-exists@email.com");
    }

    @Test
    public void getCurrentUserNotAuthorized() {
        assertNull(userService.getCurrentUser());
    }
}
