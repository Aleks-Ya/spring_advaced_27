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
        User actUserByEmail = userService.getUserByEmail(email);
        assertEquals("User should be the same", expUser, actUserByEmail);
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterSameUser() {
        User user = testObjects.createJohn();
        userService.register(user);
    }

    @Test
    public void testDelete() {
        User user = testObjects.createJohn();
        userService.delete(user);
        User actUser = userService.getUserByEmail(user.getEmail());
        assertNull("User should be the same", actUser);
    }

    @Test
    public void testGetUserByEmail() {
        User user = testObjects.createJohn();
        User foundUser = userService.getUserByEmail(user.getEmail());
        assertEquals("User should match", user, foundUser);
    }

    @Test
    public void testGetUserByEmail_Null() {
        User foundUser = userService.getUserByEmail(UUID.randomUUID().toString());
        assertNull("There should not be such user", foundUser);
    }

    @Test
    public void getCurrentUserNotAuthorized() {
        assertNull(userService.getCurrentUser());
    }
}
