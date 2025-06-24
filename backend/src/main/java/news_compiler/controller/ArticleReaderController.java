package news_compiler.controller;

import news_compiler.dto.ArticleDto;
import news_compiler.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller with various endpoints for fetching articles from the database.
 * <p>
 * The intention for this controller is to only be used by the frontend to read
 * articles, and not by the internal web scraper or other external sources.
 */
@RestController
@RequestMapping("/api/articles")
@CrossOrigin("*")  // TODO: Restrict this
public class ArticleReaderController {
    /** Service for Article entities */
    @Autowired
    private ArticleService articleService;

    /**
     * Returns articles fetched in the last 24 hours.
     * The article will be sorted by the date of publishing.
     * @return a list of the most recently fetched articles
     */
    @GetMapping("/recent")
    public List<ArticleDto> getRecentArticles() {
        return articleService.getRecentlyFetched();
    }

    /**
     * Returns a lightweight version of the articles fetched in the last 24 hours.
     * These articles do not contain the body of the article, to reduce payload size.
     * @return a list of the most recently fetched articles with a null body.
     */
    @GetMapping("/recent/lite")
    public List<ArticleDto> getRecentArticlesLite() {
        return articleService.getRecentlyFetchedLite();
    }

    /**
     * Returns an article by its ID.
     * @param id the ID of the article to fetch
     * @return the article with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id) {
        // Validate the ID
        if (id == null || id < 0)
            return ResponseEntity.badRequest().build();

        // Fetch the article
        ArticleDto article = articleService.getArticleById(id);
        if (article == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(article);
    }
}
