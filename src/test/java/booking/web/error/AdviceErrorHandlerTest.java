package booking.web.error;

import booking.BaseWebTest;
import booking.web.controller.UserController;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = UserController.class)
public class AdviceErrorHandlerTest extends BaseWebTest {

    @Test
    public void register() throws Exception {
        mvc.perform(post(UserController.ENDPOINT + "/register")//TODO put /user/register to constant
                .param("name", "John")
                .param("birthday", "2000-07-03")
                .param("password", "pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>An error occurred</h1>\n" +
                        "<p>User's email is [null]: [User {email='null', name='John', birthday='2000-07-03'}]</p>"));
    }

}