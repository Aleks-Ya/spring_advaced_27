package booking.web.freemarker;

import booking.domain.User;
import booking.service.AccountService;
import booking.service.UserService;
import freemarker.template.TemplateMethodModelEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Returns Account of current user.
 */
@Component
public class CurrentUserAccountMethod implements TemplateMethodModelEx {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public CurrentUserAccountMethod(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public Object exec(List arguments) {
        User currentUser = userService.getCurrentUser();
        return currentUser != null ? accountService.getByUserId(currentUser.getId()) : null;
    }
}
