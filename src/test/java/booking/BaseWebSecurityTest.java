package booking;

import booking.domain.User;
import booking.web.controller.LoginController;
import booking.web.security.SecurityConfig;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Parent class for unit tests that use repository, service, web and security layers.
 */
@WebAppConfiguration
@ContextConfiguration(classes = SecurityConfig.class)
public abstract class BaseWebSecurityTest extends BaseWebTest {
    @Autowired
    protected WebApplicationContext wc;
    protected MockMvc mvc;

    @Before
    @Override
    public void initMockMvc() {
        mvc = MockMvcBuilders
                .webAppContextSetup(wc)
                .apply(springSecurity())
                .build();
    }

    protected MockHttpSession authenticateSession(User user) throws Exception {
        MockHttpSession session = new MockHttpSession();

        mvc.perform(post(LoginController.ENDPOINT).session(session)
                .param("username", user.getEmail())
                .param("password", testObjects.getRawPassword(user.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andExpect(status().is3xxRedirection());

        return session;
    }

    protected void assertRedirectToLoginPage(MockHttpServletRequestBuilder builder) {
        try {
            MvcResult mvcResult = mvc.perform(builder)
                    .andExpect(status().is3xxRedirection())
                    .andReturn();
            String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();
            assertThat(redirectedUrl, endsWith(LoginController.ENDPOINT));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}