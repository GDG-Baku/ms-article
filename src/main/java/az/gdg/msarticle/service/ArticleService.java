package az.gdg.msarticle.service;

import az.gdg.msarticle.model.ArticleRequest;

public interface ArticleService {
    String updateArticle(String id, ArticleRequest articleRequest);
}
