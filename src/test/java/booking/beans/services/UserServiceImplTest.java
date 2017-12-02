package booking.beans.services;

import booking.beans.config.PropertySourceConfig;
import booking.beans.config.TestUserServiceConfig;
import booking.beans.config.db.DataSourceConfig;
import booking.beans.config.db.DbSessionFactoryConfig;
import booking.beans.daos.mocks.UserDAOMock;
import booking.beans.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 8:02 PM
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PropertySourceConfig.class, DataSourceConfig.class, DbSessionFactoryConfig.class, TestUserServiceConfig.class})
@Transactional
public class UserServiceImplTest {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    @Value("#{testUserServiceImpl}")
    private UserService userService;

    @Autowired
    private UserDAOMock userDAOMock;

    @Before
    public void init() {
        userDAOMock.init();
    }

    @After
    public void cleanup() {
        userDAOMock.cleanup();
    }

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
    public void testRegister_Exception() {
        User testUser1 = (User) applicationContext.getBean("testUser1");
        userService.register(testUser1);
    }

    @Test
    public void testRemove() {
        User testUser1 = (User) applicationContext.getBean("testUser1");
        userService.remove(testUser1);
        assertEquals("User should be the same", userService.getUserByEmail(testUser1.getEmail()), null);
    }

    @Test
    public void testUsersGetByName() {
        User testUser1 = (User) applicationContext.getBean("testUser1");
        List<User> before = userService.getUsersByName(testUser1.getName());
        User addedUser = new User(UUID.randomUUID().toString(), testUser1.getName(), LocalDate.now(), "pass", null);
        long registeredId = userService.register(addedUser).getId();
        List<User> after = userService.getUsersByName(testUser1.getName());
        before.add(addedUser.withId(registeredId));
        assertTrue("Users should change", before.containsAll(after));
        assertTrue("Users should change", after.containsAll(before));
    }

    @Test
    public void testGetUserByEmail() {
        User testUser1 = (User) applicationContext.getBean("testUser1");
        User foundUser = userService.getUserByEmail(testUser1.getEmail());
        assertEquals("User should match", testUser1, foundUser);
    }

    @Test
    public void testGetUserByEmail_Null() {
        User foundUser = userService.getUserByEmail(UUID.randomUUID().toString());
        assertNull("There should not be such user", foundUser);
    }
}
