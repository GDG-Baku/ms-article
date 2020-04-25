package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.ArticleNotFound;
import az.gdg.msarticle.exception.NotValidTokenException;
import az.gdg.msarticle.mapper.ArticleMapper;
import az.gdg.msarticle.model.ArticleRequest;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.service.ArticleService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new NotValidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String createDraft(ArticleRequest articleRequest) {
        ArticleEntity articleEntity = ArticleMapper.INSTANCE.requestToEntity(articleRequest);
        articleEntity.setDraft(true);
        articleEntity.setUserId(31);
        articleRepository.save(articleEntity);
        return "Article is drafted";
    }

    @Override
    public String publishArticle(String articleId) {
        String userId = (String) getAuthenticatedObject().getPrincipal();
        String message;
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFound("Article doesn't exist with this id" + articleId));

        if (articleEntity.getUserId() == Integer.parseInt(userId)) {
            if (articleEntity.isDraft()) {
                articleEntity.setDraft(false);
                message = "Article is published now";
            } else {
                message = "Article is already published";
            }
            articleRepository.save(articleEntity);
            return message;
        }
        return "You don't have permission for this";
    }
}
