package booking.web.freemarker;

import booking.service.UserService;
import freemarker.template.TemplateMethodModelEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrentUserMethod implements TemplateMethodModelEx {

    private final UserService userService;

    @Autowired
    public CurrentUserMethod(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object exec(List arguments) {
        return userService.getCurrentUser();
    }
}
