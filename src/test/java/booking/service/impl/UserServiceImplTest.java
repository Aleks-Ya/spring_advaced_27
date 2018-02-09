package booking.service.impl;

import booking.BaseServiceTest;
import booking.domain.User;
import org.junit.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

    @Test(expected = RuntimeException.class)
    public void testRegisterSameUser() {
        User user = to.createJohn();
        userService.register(user);
    }

    @Test
    public void testDelete() {
        User user = to.createJohn();
        userService.delete(user);
        User actUser = userService.getByEmail(user.getEmail());
        assertNull(actUser);
    }

    @Test
    public void testGetUserByEmail() {
        User user = to.createJohn();
        User foundUser = userService.getByEmail(user.getEmail());
        assertEquals("User should match", user, foundUser);
    }

    @Test
    public void testGetUserByEmail_Null() {
        User foundUser = userService.getByEmail(UUID.randomUUID().toString());
        assertNull("There should not be such user", foundUser);
    }

    @Test
    public void getCurrentUserNotAuthorized() {
        assertNull(userService.getCurrentUser());
    }
}
