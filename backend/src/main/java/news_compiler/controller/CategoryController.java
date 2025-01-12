package news_compiler.controller;

import news_compiler.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller with various endpoint(s) for the categories of news articles.
 * <p>
 * The intention for this controller is to be used by the website to display UI elements for
 * displaying news articles by category.
 */
@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")  // TODO: Restrict this
public class CategoryController {
    /** Service for Category entities */
    @Autowired
    private CategoryService categoryService;

    /**
     * Returns a list of all valid categories an article may be part of.
     * @return a list of all valid categories
     */
    @GetMapping
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }
}
