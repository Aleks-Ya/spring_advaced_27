package booking.web.rest;

import booking.domain.User;
import booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static booking.web.rest.RestConfig.REST_ROOT_ENDPOINT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@SuppressWarnings("unused")
public class UserRestController {
    public static final String ENDPOINT = REST_ROOT_ENDPOINT + "/user";

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = ENDPOINT + "/{userId}", method = GET)
    User getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @RequestMapping(path = ENDPOINT, method = GET)
    List<User> getAll() {
        return userService.getAll();
    }
}
