package az.gdg.article.service;

import az.gdg.article.model.ArticleRequest;

public interface ArticleService {

    String addDraft(String token, ArticleRequest articleRequest);

}
