package news_compiler.service;

import news_compiler.dto.ArticleDto;
import news_compiler.entity.Article;
import news_compiler.mapper.ArticleMapper;
import news_compiler.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Service for Article entities.
 * This class contains methods that are used to manipulate Article entities.
 * The controller will use this service to interact with the database.
 */
@Service
public class ArticleService {
    /** Repository for Article entities */
    @Autowired
    private ArticleRepository articleRepository;

    /**
     * Returns an article by its ID.
     *
     * @param id the ID of the article to fetch
     * @return the article with the given ID
     */
    public ArticleDto getArticleById(Long id) {
        // Validate the ID
        if (id == null || id < 0)
            throw new IllegalArgumentException("Invalid ID");

        // Fetch the article
        Optional<Article> article = articleRepository.findById(id);
        return article.map(ArticleMapper::articleToDto).orElse(null);
    }

    /**
     * Returns articles fetched in the last 24 hours.
     * The article will be sorted by date of publishing.
     *
     * @return a list of the most recently fetched articles
     */
    public List<ArticleDto> getRecentlyFetched() {
        // Fetch articles in order of publication
        List<ArticleDto> articles = articleRepository.findAll()
                .stream().map(ArticleMapper::articleToDto).toList();  // Map to DTOs

        // Get the time 24-hours ago
        OffsetDateTime time24HoursAgo = OffsetDateTime.now().minusHours(24);

        // Filter out articles that were fetched more than 24 hours ago
        articles = articles.stream()
                .filter(article -> article.getTimeFetched().toEpochSecond() > time24HoursAgo.toEpochSecond())
                .toList();

        // Create a new mutable list to sort articles
        ArrayList<ArticleDto> mutableArticles = new ArrayList<>(articles);

        // Sort articles by time published, keeping null times at the end
        mutableArticles.sort((a1, a2) -> {
            if (a1.getTimePublished() == null && a2.getTimePublished() == null) {
                // Compare based on time fetched
                return a2.getTimeFetched().compareTo(a1.getTimeFetched());
            } else if (a1.getTimePublished() == null) {
                return 1;
            } else if (a2.getTimePublished() == null) {
                return -1;
            } else {
                return a2.getTimePublished().compareTo(a1.getTimePublished());
            }
        });

        return mutableArticles;
    }

    /**
     * Returns a lightweight version of the articles fetched in the last 24 hours.
     * These articles do not contain the body of the article, to reduce payload size.
     *
     * @return a list of the most recently fetched articles with a null body.
     */
    public List<ArticleDto> getRecentlyFetchedLite() {
        List<ArticleDto> articles = getRecentlyFetched();

        // Set the body to null to reduce payload size
        articles.forEach(article -> article.setBody(null));

        return articles;
    }

    /**
     * Writes articles to the database.
     *
     * @param articles the articles to write
     * @return the articles that were written
     */
    public List<ArticleDto> writeArticles(List<ArticleDto> articles) {
        // Create a list to store the written articles
        List<ArticleDto> writtenArticles = new LinkedList<>();

        // Write each article
        for (ArticleDto article : articles)
            writtenArticles.addLast(writeArticle(article));

        // Return the articles that were written
        return writtenArticles;
    }

    /**
     * Writes a single article to the database.
     * <p>
     * <B>Note</B>: The ID of the given <code>ArticleDto</code> is disregarded.     *
     * @param articleDto the article to write
     * @return the article that was written
     */
    public ArticleDto writeArticle(ArticleDto articleDto) {
        Article article = ArticleMapper.dtoToArticle(articleDto);

        // Change the article's ID to null
        article.setId(null);

        // If the article already exists
        if (articleRepository.findByTitle(article.getTitle()) != null) {
            // Update the existing article
            Article existingArticle = articleRepository.findByTitle(article.getTitle());
            article.setId(existingArticle.getId());
        }

        // Save and return the article
        Article writtenArticle = articleRepository.save(article);
        return ArticleMapper.articleToDto(writtenArticle);
    }
}
