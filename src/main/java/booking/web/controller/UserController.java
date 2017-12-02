package booking.web.controller;

import booking.domain.models.User;
import booking.service.UserService;
import booking.util.JsonUtil;
import booking.web.security.Roles;
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
import java.time.LocalDate;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Controller
@SuppressWarnings("unused")
@RequestMapping("/user")
public class UserController {
    static final String PART_NAME = "users";
    private static final String USER_ATTR = "user";
    private static final String USER_FTL = "user/user";
    private static final String USER_REGISTERED_FTL = "user/user_registered";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private static User createUserFromNewUserData(@RequestBody NewUserData newUserData) {
        return new User(newUserData.getEmail(), newUserData.getName(), newUserData.getBirthday(),
                newUserData.getPassword(), Roles.RESGISTERED_USER);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    String register(@RequestBody NewUserData newUserData, @ModelAttribute("model") ModelMap model) {
        User newUser = createUserFromNewUserData(newUserData);
        User user = userService.register(newUser);
        model.addAttribute(USER_ATTR, user);
        return USER_REGISTERED_FTL;
    }

    @RequestMapping(value = "/id/{userId}", method = RequestMethod.GET)
    String getById(@PathVariable Long userId, @ModelAttribute("model") ModelMap model) {
        User user = userService.getById(userId);
        model.addAttribute(USER_ATTR, user);
        return USER_FTL;
    }

    @RequestMapping(path = "/batchUpload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void batchUpload(@RequestParam(value = PART_NAME) List<MultipartFile> users) throws IOException {
        for (MultipartFile userFile : users) {
            NewUserData newUserData = JsonUtil.readValue(userFile.getBytes(), NewUserData.class);
            User newUser = createUserFromNewUserData(newUserData);
            userService.register(newUser);
        }
    }

    private static class NewUserData {
        private String email;
        private String name;
        private LocalDate birthday;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
