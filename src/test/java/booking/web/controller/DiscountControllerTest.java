package booking.web.controller;

import booking.beans.configuration.StrategiesConfiguration;
import booking.beans.configuration.TestEventServiceConfiguration;
import booking.beans.configuration.TestStrategiesConfiguration;
import booking.beans.configuration.TestUserServiceConfiguration;
import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactory;
import booking.beans.services.DiscountServiceImpl;
import booking.web.FreeMarkerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {FreeMarkerConfig.class, DiscountController.class, DiscountServiceImpl.class,
        DataSourceConfiguration.class, DbSessionFactory.class,
         TestStrategiesConfiguration.class, StrategiesConfiguration.class, TestUserServiceConfiguration.class,
        TestEventServiceConfiguration.class
})
public class DiscountControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void auditoriumsGet() throws Exception {
        mvc.perform(get(DiscountController.ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Auditoriums</h1>\n" +
                        "<p>VIP seats: 25,26,27,28,29,30,31,32,33,34,35,75,76,77,78,79,80,81,82,83,84,85,105,106,107,108,109,110,111,112,113,114,115</p><hr/>\n"));
    }
}