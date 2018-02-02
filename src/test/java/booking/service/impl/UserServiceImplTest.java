package booking.service.impl;

import booking.BaseServiceTest;
import booking.domain.User;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 8:02 PM
 */
public class UserServiceImplTest extends BaseServiceTest {

    @Test
    public void testRegister() {
        String email = UUID.randomUUID().toString();
        String name = UUID.randomUUID().toString();
        User user = new User(email, name, LocalDate.now(), "mypass", "admin,user");
        User expUser = userService.register(user);

        User actUserByEmail = userService.getUserByEmail(email);

        List<User> usersByName = userService.getUsersByName(name);
        assertThat(usersByName, hasSize(1));
        User actUserByName = usersByName.get(0);

        assertEquals("User should be the same", expUser, actUserByEmail);
        assertEquals("User should be the same", expUser, actUserByName);
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
    public void testUsersGetByName() {
        User testUser1 = testObjects.createJohn();
        List<User> before = userService.getUsersByName(testUser1.getName());
        User addedUser = new User(UUID.randomUUID().toString(), testUser1.getName(),
                LocalDate.now(), "pass", null);
        long registeredId = userService.register(addedUser).getId();
        List<User> after = userService.getUsersByName(testUser1.getName());
        before.add(addedUser.withId(registeredId));
        assertTrue("Users should change", before.containsAll(after));
        assertTrue("Users should change", after.containsAll(before));
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
}
