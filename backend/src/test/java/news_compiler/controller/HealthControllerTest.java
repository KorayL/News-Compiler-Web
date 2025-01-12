package news_compiler.controller;

import news_compiler.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the endpoints in the <code>HealthController</code>.
 */
@SpringBootTest
@AutoConfigureMockMvc
class HealthControllerTest extends BaseTest {

    /** The mock MVC object for testing. */
    @Autowired
    private MockMvc mockMVC;

    /** Tests that the `ping` method functions as intended. */
    @Test
    void ping() throws Exception {
        this.mockMVC.perform(get("/api/health/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("pong")));
    }
}