package news_compiler.controller;

import com.google.gson.reflect.TypeToken;
import news_compiler.BaseTest;
import news_compiler.TestUtils;
import news_compiler.dto.ArticleDto;
import news_compiler.entity.Article;
import news_compiler.entity.Category;
import news_compiler.mapper.ArticleMapper;
import news_compiler.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the endpoints in the <code>ArticleReaderController</code>.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ArticleReaderControllerTest extends BaseTest {

    /** The mock MVC object for testing. */
    @Autowired
    private MockMvc mockMVC;

    /**
     * The repository for Article entities.
     * This will be used to add articles to the database independently of controllers or services.
     */
    @Autowired
    private ArticleRepository articleRepository;

    /**
     * A list of articles to test with.
     * These articles will be added to the database before each test.
     * This list can be used to compare against the articles fetched by the controller.
     */
    private List<ArticleDto> testArticles;

    /**
     * Set up the test environment.
     * Do this by clearing the database and adding articles to it.
     */
    @BeforeEach
    void setUp() {
        // Clear database
        articleRepository.deleteAll();

        // Generate random datetime
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime hourAgo = now.minusHours(1);
        OffsetDateTime twoHoursAgo = now.minusHours(2);

        testArticles = List.of(
                new ArticleDto(null, "title1", "body1", now, now, "source1", "url1", "image1", Category.BUSINESS),
                new ArticleDto(null, "title2", "body2", twoHoursAgo, twoHoursAgo, "source2", "url2", "image2", Category.FOOD),
                new ArticleDto(null, "title3", "body3", hourAgo, hourAgo, "source3", "url3", "image3", Category.FOOD)
        );

        // Save the test articles to the database
        articleRepository.saveAll(testArticles.stream().map(ArticleMapper::dtoToArticle).toList());
        assertEquals(3, articleRepository.count());
    }

    /** Tests that fields are injected properly. */
    @Test
    void contextLoads() {
        assertThat(articleRepository).isNotNull();
    }

    /**
     * Tests that the <code>getRecentArticles</code> endpoint functions as intended.
     * @throws Exception if the test fails
     */
    @Test
    @Transactional
    void getRecentArticles() throws Exception {
        // Ensure that the controller returns the correct articles
        mockMVC.perform(get("/api/articles/recent"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testArticles.get(0).getTitle())))
                .andExpect(content().string(containsString(testArticles.get(2).getTitle())))
                .andExpect(content().string(containsString(testArticles.get(1).getTitle())));
    }

    /**
     * Tests that the <code>getRecentArticles</code> endpoint functions as intended when the
     * database is empty.
     * @throws Exception if the test fails
     */
    @Test
    @Transactional
    void getRecentArticlesEmpty() throws Exception {
        // Clear the database
        articleRepository.deleteAll();

        // Ensure that the controller returns an empty list
        mockMVC.perform(get("/api/articles/recent"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    /**
     * Ensures that the <code>getRecentArticles</code> endpoint returns articles in the correct
     * order.
     * @throws Exception if there is an issue with the GET request
     */
    @Test
    @Transactional
    void getRecentArticlesCheckOrder() throws Exception {
        // Ensure that the controller returns the correct articles in the correct order
        String resultString = mockMVC.perform(get("/api/articles/recent"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Ensure that the articles are in the correct order
        assert(resultString.indexOf(testArticles.get(0).getTitle()) < resultString.indexOf(testArticles.get(2).getTitle()));
        assert(resultString.indexOf(testArticles.get(2).getTitle()) < resultString.indexOf(testArticles.get(1).getTitle()));

        // Add a null time into the database
        Article nullTImeArticle = new Article(null, "title4", "body4", null, OffsetDateTime.now(), "source4", "url4", "image4", Category.FOOD);
        articleRepository.save(nullTImeArticle);

        // Ensure that the controller returns the correct articles in the correct order
        resultString = mockMVC.perform(get("/api/articles/recent"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Ensure that the articles are in the correct order
        assert(resultString.indexOf(testArticles.get(0).getTitle()) < resultString.indexOf(testArticles.get(2).getTitle()));
        assert(resultString.indexOf(testArticles.get(2).getTitle()) < resultString.indexOf(testArticles.get(1).getTitle()));
        assert(resultString.indexOf(testArticles.get(1).getTitle()) < resultString.indexOf("title4"));
    }

    /**
     * Tests that the <code>getRecentArticlesOver24Hours</code> endpoint functions as intended
     * when there are articles in the database fetched over 24 hours ago.
     * @throws Exception if there is an issue with the GET request
     */
    @Test
    @Transactional
    void getRecentArticlesOver24Hours() throws Exception {
        // Make some older articles
        final OffsetDateTime now = OffsetDateTime.now();
        final OffsetDateTime dayAgo = now.minusDays(1);
        final OffsetDateTime twoDaysAgo = now.minusDays(2);

        final ArticleDto article1 = new ArticleDto(null, "title4", "body4", dayAgo, dayAgo, "source4", "url4", "image4", Category.FOOD);
        final ArticleDto article2 = new ArticleDto(null, "title5", "body5", twoDaysAgo, twoDaysAgo, "source5", "url5", "image5", Category.FOOD);

        articleRepository.saveAll(List.of(ArticleMapper.dtoToArticle(article1), ArticleMapper.dtoToArticle(article2)));

        // Ensure that the controller returns the correct articles
        mockMVC.perform(get("/api/articles/recent"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testArticles.get(0).getTitle())))
                .andExpect(content().string(containsString(testArticles.get(2).getTitle())))
                .andExpect(content().string(containsString(testArticles.get(1).getTitle())))
                .andExpect(content().string(not(containsString("title4"))))
                .andExpect(content().string(not(containsString("title5"))));
    }

    /**
     * Tests that the <code>getArticleById</code> endpoint functions as intended.
     * <p>
     * This test relies on the <code>getRecentArticles</code> test to pass.
     * @throws Exception if the test fails or if there is an issue with the GET request
     */
    @Test
    @Transactional
    void getArticleById() throws Exception {
        final String resultString = mockMVC.perform(get("/api/articles/recent"))
                .andExpect(status().isOk())  // Ensure that the request was successful
                .andReturn().getResponse().getContentAsString();  // Get the response as a string

        // Parse the resulting string into a list of ArticleDto objects
        final Type listType = new TypeToken<List<ArticleDto>>() {}.getType();
        final List<ArticleDto> resultArticles = TestUtils.gson.fromJson(resultString, listType);

        // Get an article to test on
        final ArticleDto article = resultArticles.get(1);
        final String articleId = article.getId().toString();

        // Attempt to fetch the article by its ID
        mockMVC.perform(get("/api/articles/" + articleId))
                .andExpect(status().isOk())  // Ensure that the request was successful
                .andExpect(content().string(containsString(article.getTitle())));  // Ensure that the article was fetched
    }

    /**
     * Tests that the <code>getArticleById</code> endpoint functions as intended when the ID is invalid.
     * @throws Exception if the test fails or if there is an issue with the GET request
     */
    @Test
    @Transactional
    void getArticleByIdInvalid() throws Exception {
        // Attempt to fetch an article with an invalid ID
        mockMVC.perform(get("/api/articles/-1"))
                .andExpect(status().isBadRequest());  // Ensure that the request was unsuccessful

        // Attempt to fetch an article with a null ID
        mockMVC.perform(get("/api/articles/null"))
                .andExpect(status().isBadRequest());  // Ensure that the request was unsuccessful

        // Attempt to fetch an article with a non-existent ID
        mockMVC.perform(get("/api/articles/1000"))
                .andExpect(status().isNotFound());  // Ensure that the request was unsuccessful
    }

    /**
     * Tests that the <code>getRecentArticlesLite</code> endpoint functions as intended.
     * This test will not check many of the contents of the articles or the edge cases around
     * fetching articles, as those are already covered by other tests.
     * This test will only check that the body of the articles is null to reduce payload size.
     * @throws Exception if the test fails or if there is an issue with the GET request
     */
    @Test
    @Transactional
    void getRecentArticlesLite() throws Exception {
        // Ensure that the controller returns the correct articles
        mockMVC.perform(get("/api/articles/recent/lite"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testArticles.get(0).getTitle())))
                .andExpect(content().string(containsString(testArticles.get(2).getTitle())))
                .andExpect(content().string(containsString(testArticles.get(1).getTitle())))
                .andExpect(content().string(not(containsString(testArticles.get(0).getBody()))))
                .andExpect(content().string(not(containsString(testArticles.get(2).getBody()))))
                .andExpect(content().string(not(containsString(testArticles.get(1).getBody()))));
    }
}
