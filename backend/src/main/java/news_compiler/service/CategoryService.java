package news_compiler.service;

import news_compiler.entity.Category;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Service for Category entities.
 * This class contains methods that are used to manipulate Category entities.
 */
@Service
public class CategoryService {
    /**
     * Generates a list of all valid categories an article may be part of.
     * @return a list of all valid categories
     */
    public List<String> getCategories() {
        return Arrays.stream(Category.values())
                .map(Enum::name)  // Grab the name of the enum
                .map(this::formatCategory)  // Format the name
                .toList();
    }

    /**
     * Formats a category string to be more human-readable.
     * Categories are stored in the database as uppercase with underscores.
     * This method replaces underscores with spaces and capitalizes the first letter of each word.
     * @param category the category to format
     * @return the formatted category
     */
    private String formatCategory(String category) {
        String[] words = category.toLowerCase().split("_");
        StringBuilder formatted = new StringBuilder();

        // Capitalize the first letter of each word and add a space
        for (String word: words) {
            formatted.append(word.substring(0, 1).toUpperCase())
                    .append(word.substring(1))
                    .append(" ");
        }

        // Return the string with beginning and trailing whitespace removed
        return formatted.toString().trim();
    }
}
