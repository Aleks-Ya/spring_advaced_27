package booking.web.controller;

import booking.beans.models.User;
import booking.beans.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Aleksey Yablokov
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final String USER_ATTRIBUTE = "user";
    private static final String USER_FTL = "user/user";
    private static final String USER_REGISTERED_FTL = "user/user_registered";

    @Autowired
    private UserService userService;

    @SuppressWarnings("unused")
    @RequestMapping(method = RequestMethod.PUT)
    String register(@RequestBody User newUser, @ModelAttribute("model") ModelMap model) {
        User user = userService.register(newUser);
        model.addAttribute(USER_ATTRIBUTE, user);
        return USER_REGISTERED_FTL;
    }

    @SuppressWarnings("unused")
    @RequestMapping(value = "/id/{userId}", method = RequestMethod.GET)
    String getById(@PathVariable Long userId, @ModelAttribute("model") ModelMap model) {
        User user = userService.getById(userId);
        model.addAttribute(USER_ATTRIBUTE, user);
        return USER_FTL;
    }
}
