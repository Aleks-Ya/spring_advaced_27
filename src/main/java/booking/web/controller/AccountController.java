package booking.web.controller;

import booking.domain.Account;
import booking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Controller
@SuppressWarnings("unused")
public class AccountController {
    public static final String ROOT_ENDPOINT = "/account";
    public static final String REFILLED_ENDPOINT = ROOT_ENDPOINT + "/refilled";

    private static final String USER_ATTR = "user";
    private static final String ACCOUNT_BEFORE_ATTR = "accountBefore";
    private static final String ACCOUNT_AFTER_ATTR = "accountAfter";
    private static final String AMOUNT_ATTR = "amount";

    private static final String REFILLING_FTL = "account/refilling";
    private static final String REFILLED_FTL = "account/refilled";

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(path = ROOT_ENDPOINT, method = RequestMethod.POST, consumes = APPLICATION_FORM_URLENCODED_VALUE)
    String refill(@RequestBody MultiValueMap<String, String> formParams,
                  @ModelAttribute(ControllerConfig.MODEL_ATTR) ModelMap model) {
        long userId = Long.parseLong(formParams.getFirst("userId"));
        Account account = accountService.getByUserId(userId);
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(formParams.getFirst("amount")));
        Account refilledAccount = accountService.refill(account, amount);

        model.addAttribute(USER_ATTR, refilledAccount.getUser());
        model.addAttribute(ACCOUNT_BEFORE_ATTR, account);
        model.addAttribute(ACCOUNT_AFTER_ATTR, refilledAccount);
        model.addAttribute(AMOUNT_ATTR, amount);
        return REFILLED_FTL;
    }

    @RequestMapping(path = ROOT_ENDPOINT, method = RequestMethod.GET)
    String getRefillingPage(@ModelAttribute(ControllerConfig.MODEL_ATTR) ModelMap model) {
        return REFILLING_FTL;
    }
}
