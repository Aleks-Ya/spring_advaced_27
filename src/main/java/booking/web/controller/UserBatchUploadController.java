package booking.web.controller;

import booking.domain.User;
import booking.service.UserService;
import booking.util.JsonUtil;
import booking.web.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
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
public class UserBatchUploadController {
    public static final String BATCH_UPLOAD_ENDPOINT = UserController.ROOT_ENDPOINT + "/batchUpload";
    static final String PART_NAME = "users";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserBatchUploadController(
            @Autowired UserService userService,
            @Autowired(required = false) PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @RequestMapping(path = BATCH_UPLOAD_ENDPOINT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void batchUpload(@RequestParam(value = PART_NAME) List<MultipartFile> users) throws IOException {
        for (MultipartFile userFile : users) {
            NewUserData newUserData = JsonUtil.readValue(userFile.getBytes(), NewUserData.class);
            String encodedPassword = UserController.encodePassword(passwordEncoder, newUserData.getPassword());
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
