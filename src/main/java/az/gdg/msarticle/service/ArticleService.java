package az.gdg.msarticle.service;

import az.gdg.msarticle.model.ArticleRequest;

public interface ArticleService {
    String createDraft(ArticleRequest articleRequest);

    String publishArticle(String articleId);
}
