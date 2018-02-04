package booking.web.controller;

import booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static booking.web.controller.RootController.ENDPOINT;
import static booking.web.controller.UserController.USER_ATTR;

/**
 * @author Aleksey Yablokov
 */
@Controller
@SuppressWarnings("unused")
@RequestMapping(value = ENDPOINT)
public class RootController {
    static final String ENDPOINT = "/";
    private static final String ROOT_FTL = "root";
    private final UserService userService;

    @Autowired
    public RootController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    String getRootPage(@ModelAttribute(ControllerConfig.MODEL_ATTR) ModelMap model) {
        model.addAttribute(USER_ATTR, userService.getCurrentUser());
        return ROOT_FTL;
    }
}
