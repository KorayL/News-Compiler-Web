package news_compiler.controller;

import com.google.gson.Gson;
import news_compiler.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the endpoints in the <code>CategoryController</code>.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest extends BaseTest {

    /** The mock MVC object for testing. */
    @Autowired
    private MockMvc mockMVC;

    @Test
    void getCategories() throws Exception {
        String resultString = mockMVC.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> categories = new Gson().fromJson(resultString, listType);

        assertThat(categories)
                .contains("United States Politics")
                .contains("World Politics")
                .contains("Science")
                .contains("Technology")
                .contains("Sports")
                .contains("Entertainment")
                .contains("Business")
                .contains("Health")
                .contains("Education")
                .contains("Environment")
                .contains("Travel")
                .contains("Food")
                .contains("Lifestyle")
                .contains("Opinion")
                .contains("Other");
    }
}
