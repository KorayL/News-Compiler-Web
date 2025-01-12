package news_compiler.mapper;

import news_compiler.dto.ArticleDto;
import news_compiler.entity.Article;

/**
 * Mapper class for the Article and ArticleDto classes
 * `Article` is a DAO.
 */
public class ArticleMapper {
    /**
     * Maps an Article DAO to an ArticleDto
     * @param article the Article to map
     * @return the Article as an ArticleDto
     */
    public static ArticleDto articleToDto(Article article) {
        return new ArticleDto(
            article.getId(),
            article.getTitle(),
            article.getBody(),
            article.getTimePublished(),
            article.getTimeFetched(),
            article.getSource(),
            article.getArticleUrl(),
            article.getImageUrl(),
            article.getCategory()
        );
    }

    /**
     * Maps an ArticleDto to an Article DAO
     * @param articleDto the ArticleDto to map
     * @return the ArticleDto as an Article
     */
    public static Article dtoToArticle(ArticleDto articleDto) {
        return new Article(
            articleDto.getId(),
            articleDto.getTitle(),
            articleDto.getBody(),
            articleDto.getTimePublished(),
            articleDto.getTimeFetched(),
            articleDto.getSource(),
            articleDto.getArticleUrl(),
            articleDto.getImageUrl(),
            articleDto.getCategory()
        );
    }
}
