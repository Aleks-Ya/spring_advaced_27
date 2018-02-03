package booking.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Aleksey Yablokov
 */
@Controller
@SuppressWarnings("unused")
public class LoginController {
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String ACCESS_DENIED_ENDPOINT = "/access_denied";
    private static final String LOGIN_FTL = "login/login";
    private static final String ACCESS_DENIED_FTL = "login/access_denied";
    private static final String ERROR_ATTR = "error";
    private static final String LOGOUT_ATTR = "logout";

    @RequestMapping(value = LOGIN_ENDPOINT, method = RequestMethod.GET)
    String getLoginPage(
            @RequestParam Map<String, String> params,
            @ModelAttribute("model") ModelMap model
    ) {
        model.addAttribute(ERROR_ATTR, params.containsKey("error"));
        model.addAttribute(LOGOUT_ATTR, params.containsKey("logout"));
        return LOGIN_FTL;
    }

    @RequestMapping(value = ACCESS_DENIED_ENDPOINT)
    String getAccessDeniedPage() {
        return ACCESS_DENIED_FTL;
    }

}
