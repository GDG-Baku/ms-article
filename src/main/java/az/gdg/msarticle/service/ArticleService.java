package az.gdg.msarticle.service;

import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.model.dto.UserArticleDTO;

public interface ArticleService {

    ArticleDTO getArticleById(String articleId);

    void deleteArticleById(String articleId);

    void addReadCount(String articleId);

    String publishArticle(String articleId);

    UserArticleDTO getArticlesByUserId(Long userId, int page);
}