package booking.web.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice("booking.web")
public class AdviceErrorHandler {
    private static final String ERROR_MESSAGE_ATTR = "errorMessage";
    private static final String ERROR_FTL = "error/error";

    @SuppressWarnings("unused")
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Exception e) {
        Map<String, String> model = new HashMap<>();
        model.put(ERROR_MESSAGE_ATTR, e.getMessage());
        return new ModelAndView(ERROR_FTL, model);
    }

}
