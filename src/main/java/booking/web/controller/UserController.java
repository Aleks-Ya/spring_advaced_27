package booking.web.controller;

import booking.domain.User;
import booking.service.UserService;
import booking.web.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Controller
@SuppressWarnings("unused")
public class UserController {
    public static final String ROOT_ENDPOINT = "/user";
    public static final String REGISTER_ENDPOINT = ROOT_ENDPOINT + "/register";
    public static final String SHOW_ALL_USERS_ENDPOINT = ROOT_ENDPOINT + "/all";

    static final String USER_ATTR = "user";
    private static final String USERS_ATTR = "users";

    private static final String USER_FTL = "user/user";
    private static final String USERS_FTL = "user/users";
    private static final String USER_REGISTERED_FTL = "user/user_registered";

    private final UserService userService;

    public UserController(
            @Autowired UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = REGISTER_ENDPOINT, method = RequestMethod.POST, consumes = APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    String register(@RequestBody MultiValueMap<String, String> formParams,
                    @ModelAttribute(ControllerConfig.MODEL_ATTR) ModelMap model) {
        String name = formParams.getFirst("name");
        String email = formParams.getFirst("email");
        String birthday = formParams.getFirst("birthday");
        String password = formParams.getFirst("password");
        User newUser = new User(email, name, LocalDate.parse(birthday), password, Roles.REGISTERED_USER);
        User user = userService.register(newUser);
        model.addAttribute(USER_ATTR, user);
        return USER_REGISTERED_FTL;
    }

    @RequestMapping(value = ROOT_ENDPOINT + "/{userId}", method = RequestMethod.GET)
    String getById(@PathVariable Long userId, @ModelAttribute(ControllerConfig.MODEL_ATTR) ModelMap model) {
        User user = userService.getById(userId);
        model.addAttribute(USER_ATTR, user);
        return USER_FTL;
    }

    @RequestMapping(path = SHOW_ALL_USERS_ENDPOINT, method = RequestMethod.GET)
    String getAll(@ModelAttribute(ControllerConfig.MODEL_ATTR) ModelMap model) {
        List<User> users = userService.getAll();
        model.addAttribute(USERS_ATTR, users);
        return USERS_FTL;
    }
}
