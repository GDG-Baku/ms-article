package az.gdg.msarticle.service;

import az.gdg.msarticle.model.dto.ArticleDTO;

public interface ArticleService {

    ArticleDTO getArticleById(String articleId);

}
