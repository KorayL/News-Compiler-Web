package news_compiler.controller;

import news_compiler.dto.ArticleDto;
import news_compiler.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller with various endpoints for writing articles to the database.
 * <p>
 * The intention for this controller is to only be used by the internal web scraper to store
 * articles, and not by the frontend or other external sources.
 */
@RestController
@RequestMapping("/api/articles")
@CrossOrigin("*")  // TODO: Restrict this
public class ArticleWriterController {
    /** Service for Article entities */
    @Autowired
    private ArticleService articleService;

    /**
     * Writes articles to the database.
     * @param articles a list of articles to write
     * @return a list of the written articles
     */
    @PostMapping
    public ResponseEntity<List<ArticleDto>> writeArticles(@RequestBody List<ArticleDto> articles) {
        return ResponseEntity.ok(articleService.writeArticles(articles));
    }
}
