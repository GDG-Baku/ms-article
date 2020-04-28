package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.NoAccessException;
import az.gdg.msarticle.exception.NoSuchArticleException;
import az.gdg.msarticle.exception.NotValidTokenException;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class ArticleServiceImpl implements ArticleService{
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;


    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    @Override
    public void deleteArticleById(String articleID) {
        logger.info("ActionLog.deleteArticleById.start");
        int userId = Integer.parseInt((String)getAuthenticatedObject().getPrincipal());
        int articleUserId = articleRepository.findById(articleID)
                .orElseThrow(()-> new NoSuchArticleException("Article doesn't exist")).getUserId();
        if(userId == articleUserId) {
            articleRepository.deleteById(articleID);
        } else {
            throw new NoAccessException("You don't have permission to delete the article");
        }
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new NotValidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
