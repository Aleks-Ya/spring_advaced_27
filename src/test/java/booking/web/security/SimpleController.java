package booking.web.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SuppressWarnings("unused")
class SimpleController {

    @ResponseBody
    @RequestMapping("/abc")
    String getAbc() {
        return "abc";
    }

}
