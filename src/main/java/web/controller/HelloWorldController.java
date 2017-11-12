package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Aleksey Yablokov
 */
@Controller
public class HelloWorldController {

    public static final String ENDPOINT = "/hello";

    @RequestMapping(ENDPOINT)
    @SuppressWarnings("unused")
    String hello() {
        return "hello";
    }
}
