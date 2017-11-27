package booking.web.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Aleksey Yablokov
 */
@Controller
@SuppressWarnings("unused")
public class SimpleController {

    @RequestMapping("/abc")
    String getAbc() {
        return "abc";
    }

}
