package booking.web.controller;

import booking.beans.models.User;
import booking.beans.services.UserService;
import booking.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Controller
@SuppressWarnings("unused")
@RequestMapping("/user")
public class UserController {
    static final String PART_NAME = "users";
    private static final String USER_ATTRIBUTE = "user";
    private static final String USER_FTL = "user/user";
    private static final String USER_REGISTERED_FTL = "user/user_registered";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.PUT)
    String register(@RequestBody User newUser, @ModelAttribute("model") ModelMap model) {
        User user = userService.register(newUser);
        model.addAttribute(USER_ATTRIBUTE, user);
        return USER_REGISTERED_FTL;
    }

    @RequestMapping(value = "/id/{userId}", method = RequestMethod.GET)
    String getById(@PathVariable Long userId, @ModelAttribute("model") ModelMap model) {
        User user = userService.getById(userId);
        model.addAttribute(USER_ATTRIBUTE, user);
        return USER_FTL;
    }

    @RequestMapping(path = "/batchUpload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void multipartUpload(@RequestParam(value = PART_NAME) List<MultipartFile> users) throws IOException {
        for (MultipartFile userFile : users) {
            User user = JsonUtil.readValue(userFile.getBytes(), User.class);
            userService.register(user);
        }
    }
}
