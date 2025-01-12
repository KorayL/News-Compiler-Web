package news_compiler.repository;

import news_compiler.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Article entities.
 * This interface extends JpaRepository, which provides all the CRUD operations on the Article table.
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    /**
     * Returns all articles in the database, ordered by date at which they were fetched.
     * @return a list of all articles in the database, ordered by date at which they were fetched
     */
    @Query("SELECT a FROM Article a ORDER BY a.timeFetched DESC")
    List<Article> findAllByOrderByDatePublishedDesc();

    /**
     * Returns the article with the given title.
     *
     * @param title the title of the article
     * @return the article with the given title
     */
    Article findByTitle(String title);
}
