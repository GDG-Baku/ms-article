package az.gdg.msarticle.service;

import az.gdg.msarticle.model.dto.ArticleDTO;

public interface ArticleService {

    ArticleDTO getArticleById(String articleId);

    void deleteArticleById(String articleId);

    void addReadCount(String articleId);

    String publishArticle(String articleId);

    UserArticleDTO getArticlesByUserId(Integer userId, int page);
}