package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Aleksey Yablokov
 */
@Controller
public class HelloWorldController {

    public static final String ENDPOINT = "/hello";
    public static final String BODY = "Hello, World!";

    @ResponseBody
    @RequestMapping(ENDPOINT)
    @SuppressWarnings("unused")
    String hello() {
        return BODY;
    }
}
