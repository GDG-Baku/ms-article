package az.gdg.msarticle.service;

import az.gdg.msarticle.model.dto.UserArticleDTO;

public interface ArticleService {

    UserArticleDTO getArticlesByUserId(String articleId);

}
