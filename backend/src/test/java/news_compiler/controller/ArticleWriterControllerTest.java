package news_compiler.controller;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static news_compiler.TestUtils.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the endpoints in the <code>ArticleWriterController</code>.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ArticleWriterControllerTest extends BaseTest {
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
     * Type of incoming JSON from the controller.
     * Used for <Code>Gson</Code> to parse the result string.
     */
    final Type listType = new TypeToken<List<ArticleDto>>() {}.getType();

    /**
     * Set up the test environment.
     * Do this by clearing the database.
     */
    @BeforeEach
    void setUp() {
        // Clear database
        articleRepository.deleteAll();

        // Ensure the database is actually empty
        assertEquals(0, articleRepository.count());

        // Generate random datetime
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime yesterday = now.minusDays(1);
        OffsetDateTime twoDaysAgo = now.minusDays(2);

        testArticles = List.of(
                new ArticleDto(null, "title1", "body1", now, now, "source1", "url1", "image1", Category.BUSINESS),
                new ArticleDto(null, "title2", "body2", twoDaysAgo, twoDaysAgo, "source2", "url2", "image2", Category.FOOD),
                new ArticleDto(null, "title3", "body3", yesterday, yesterday, "source3", "url3", "image3", Category.FOOD)
        );
    }

    /** Tests that fields are injected properly. */
    @Test
    void contextLoads() {
        assertThat(articleRepository).isNotNull();
    }

    /**
     * Tests that the <code>writeArticles</code> method functions as intended when writing a single
     * article.
     * @throws Exception if the request fails
     */
    @Test
    @Transactional
    void writeArticlesSingle() throws Exception {
        // Convert the test article to a JSON string
        ArticleDto testArticle = testArticles.getFirst();
        String body = asJsonString(List.of(testArticle));

        // Remove the ID tag from the JSON string
        body = body.replaceFirst("\"id\":null,", "");
        assert(!body.contains("\"id\":"));

        // Call the endpoint
        String resultString = mockMVC.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Parse the result
        List<ArticleDto> result = TestUtils.gson.fromJson(resultString, listType);
        assertEquals(1, result.size());

        // Get the article from the result
        ArticleDto resultArticle = result.getFirst();

        // Check that only one article is in the database
        assertEquals(1, articleRepository.count());

        // Check that the article was written to the database
        Optional<Article> writtenArticle = articleRepository.findById(resultArticle.getId());
        assertTrue(writtenArticle.isPresent());

        // Check that the article was written correctly
        assertThat(ArticleMapper.articleToDto(writtenArticle.get())).isEqualTo(testArticle);

        // Check that the returned article matches
        assertThat(resultArticle).isEqualTo(testArticle);
    }

    /**
     * Tests that the <code>writeArticles</code> method functions as intended when writing multiple
     * articles.
     * @throws Exception if the request fails
     */
    @Test
    @Transactional
    void writeArticlesMultiple() throws Exception {
        // Convert the test articles to a JSON string
        String body = asJsonString(this.testArticles);

        // Remove the ID tags from the JSON string
        body = body.replaceAll("\"id\":null,", "");
        assert(!body.contains("\"id\":"));

        // Call the endpoint
        String resultString = mockMVC.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Parse the result
        List<ArticleDto> result = TestUtils.gson.fromJson(resultString, listType);
        assertEquals(testArticles.size(), result.size());

        // Compare returned and saved articles
        assertEquals(articleRepository.count(), result.size());
        for (int i = 0; i < testArticles.size(); ++i) {
            ArticleDto article = testArticles.get(i);

            // Check the returned article
            assertEquals(result.get(i), article, "Data saved incorrectly or article was" +
                    "returned in the wrong order.");

            // Check article saved to the repository
            ArticleDto writtenArticle =
                    ArticleMapper.articleToDto(articleRepository.findByTitle(article.getTitle()));
            assertThat(writtenArticle)
                    .withFailMessage("Incorrect article data when searching by title.")
                    .isEqualTo(article);
        }
    }

    /**
     * Tests the <code>writeArticles</code> method functions as intended when writing very large
     * field sizes.
     * @throws Exception if the request fails
     */
    @Test
    @Transactional
    void writeArticlesLargeFields() throws Exception {
        // Create a large article
        ArticleDto largeArticle = new ArticleDto(
                null,
                "title",
                "body".repeat(5000),
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                "source",
                "url".repeat(666),  // Approx 2000 characters (max-ish URL length)
                "image".repeat(400),  // 2000 characters (max URL length)
                Category.FOOD
        );

        // Convert the test articles to a JSON string
        String body = asJsonString(List.of(largeArticle));

        // Remove the ID tags from the JSON string
        body = body.replaceAll("\"id\":null,", "");
        assert(!body.contains("\"id\":"));

        // Call the endpoint
        String resultString = mockMVC.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Parse the result
        List<ArticleDto> result = TestUtils.gson.fromJson(resultString, listType);
        assertEquals(1, result.size());

        // Compare returned and saved articles
        assertEquals(1, articleRepository.count());
        ArticleDto writtenArticle = ArticleMapper.articleToDto(
                articleRepository.findByTitle(largeArticle.getTitle()));
        assertThat(writtenArticle).isEqualTo(largeArticle);
    }

    /**
     * Tests that the <code>writeArticles</code> method functions as intended when updating an
     * article.
     * @throws Exception if there is an issue with the request
     */
    @Test
    @Transactional
    void updateArticle() throws Exception {
        ArticleDto articleToUpdate = new ArticleDto(
                null,
                testArticles.get(1).getTitle(),
                "This is a new body",
                OffsetDateTime.now(),
                OffsetDateTime.now().minusDays(10),
                "Updated source",
                "updatedExample.com",
                "updatedExample.com/image.png",
                Category.FOOD
        );

        // Create a list of articles with the updated article
        List<ArticleDto> updatedTestArticles = new LinkedList<>();
        for (ArticleDto article : testArticles) {
            if (article.getTitle().equals(articleToUpdate.getTitle())) {
                updatedTestArticles.add(articleToUpdate);
            } else {
                updatedTestArticles.add(article);
            }
        }

        // Call the endpoint to create the articles
        mockMVC.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(testArticles)))
                .andExpect(status().isOk());

        // Call the endpoint to update the articles
        String resultString = mockMVC.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedTestArticles)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Parse the result
        List<ArticleDto> result = TestUtils.gson.fromJson(resultString, listType);

        // Ensure database still has the same number of articles
        assertEquals(testArticles.size(), articleRepository.count());

        // Ensure all articles are correct
        for (ArticleDto article : updatedTestArticles) {
            ArticleDto writtenArticle = ArticleMapper.articleToDto(
                    articleRepository.findByTitle(article.getTitle()));
            assertThat(writtenArticle).isEqualTo(article);

            assertTrue(result.contains(article));
        }
    }
}
