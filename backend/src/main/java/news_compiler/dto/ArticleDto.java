package news_compiler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import news_compiler.entity.Category;

import java.time.OffsetDateTime;

/**
 * Represents an article in the database.
 * Articles are fetched from various sources and stored in the database.
 * The philosophy behind the states stored in an article is to deliver minimal baggage
 * to the user viewing it.
 * Additionally, timestamps may be used to order articles.
 */
@Getter
@Setter
@AllArgsConstructor
public class ArticleDto {
    /** Unique identifier for the article */
    private Long id;

    /** Title of the article. Cannot be the same as another article */
    private String title;

    /** Body of the article. */
    private String body;

    /** Time the article was published. */
    private OffsetDateTime timePublished;

    /** Time the article was fetched from its source */
    private OffsetDateTime timeFetched;

    /** The outlet from which the article was fetched */
    private String source;

    /** URL to the where the article was scraped from */
    private String articleUrl;

    /** URL to the image associated with the article */
    private String imageUrl;

    /** The category of the article */
    private Category category;

    @Override
    public String toString() {
        return "ArticleDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", timePublished=" + timePublished +
                ", timeFetched=" + timeFetched +
                ", source='" + source + '\'' +
                ", articleUrl='" + articleUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", category=" + category +
                '}';
    }

    /**
     * Checks if two <code>ArticleDto</code> objects are equal. ID is not considered.
     * Time is considered equal if the times represent the same instant.
     * @param obj the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        // Ensure types align
        if (!(obj instanceof ArticleDto other)) {
            return false;
        }

        // Ensure all fields are equal except for the ID
        return  title.equals(other.title) &&
                body.equals(other.body) &&
                timePublished.isEqual(other.timePublished) &&
                timeFetched.isEqual(other.timeFetched) &&
                source.equals(other.source) &&
                articleUrl.equals(other.articleUrl) &&
                imageUrl.equals(other.imageUrl) &&
                category.equals(other.category);
    }
}
