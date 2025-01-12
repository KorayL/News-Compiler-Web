package news_compiler.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@NoArgsConstructor
@Entity
@Table(name = "articles")
public class Article {
    // SQL date type: https://www.w3schools.com/sql/sql_datatypes.asp

    /** Unique identifier for the article */
    @Id
    @GeneratedValue
    private Long id;

    /** Title of the article. Cannot be the same as another article */
    @Column(unique = true)
    private String title;

    /** Body of the article. */
    @Column(columnDefinition = "LONGTEXT")
    private String body;

    /** Time the article was published. */
    @Column(columnDefinition = "TIMESTAMP")
    private OffsetDateTime timePublished;

    /** Time the article was fetched from its source */
    @Column(columnDefinition = "TIMESTAMP")
    private OffsetDateTime timeFetched;

    /** The outlet from which the article was fetched */
    private String source;

    /** URL to the where the article was scraped from */
    @Column(columnDefinition = "VARCHAR(2000)")
    private String articleUrl;

    /** URL to the image associated with the article */
    @Column(columnDefinition = "VARCHAR(2000)")
    private String imageUrl;

    /** The category of the article */
    @Enumerated(EnumType.STRING)
    private Category category;

    /**
     * Constructor for an Article entity with no ID — null id.
     * @param title Title of the article
     * @param body Body of the article
     * @param timePublished Time the article was published
     * @param timeFetched Time the article was fetched
     * @param source The outlet from which the article was fetched
     * @param articleUrl URL to the where the article was scraped from
     * @param imageUrl URL to the image associated with the article
     * @param category The category of the article
     */
    public Article (String title, String body, OffsetDateTime timePublished, OffsetDateTime timeFetched, String source, String articleUrl, String imageUrl, Category category) {
        this(null, title, body, timePublished, timeFetched, source, articleUrl, imageUrl, category);
    }
}
