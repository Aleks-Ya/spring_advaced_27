package booking.web.controller;

import booking.domain.User;
import booking.service.UserService;
import booking.util.JsonUtil;
import booking.web.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static booking.web.controller.UserController.ENDPOINT;

/**
 * @author Aleksey Yablokov
 */
@Controller
@SuppressWarnings("unused")
@RequestMapping(ENDPOINT)
public class UserController {
    public static final String ENDPOINT = "/user";
    static final String PART_NAME = "users";
    static final String USER_ATTR = "user";
    private static final String USER_FTL = "user/user";
    private static final String USER_REGISTERED_FTL = "user/user_registered";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    @ResponseStatus(HttpStatus.CREATED)
    String register(@RequestBody MultiValueMap<String, String> formParams, @ModelAttribute("model") ModelMap model) {
        String name = formParams.getFirst("name");
        String email = formParams.getFirst("email");
        String birthday = formParams.getFirst("birthday");
        String rawPassword = formParams.getFirst("password");
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User newUser = new User(email, name, LocalDate.parse(birthday), encodedPassword, Roles.REGISTERED_USER);
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
            String encodedPassword = passwordEncoder.encode(newUserData.getPassword());
            User newUser = new User(newUserData.getEmail(), newUserData.getName(), newUserData.getBirthday(),
                    encodedPassword, Roles.REGISTERED_USER);
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
