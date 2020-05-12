package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.NoAccessException;
import az.gdg.msarticle.exception.NoSuchArticleException;
import az.gdg.msarticle.exception.NotValidTokenException;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.repository.CommentRepository;
import az.gdg.msarticle.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;


    public ArticleServiceImpl(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }


    @Override
    public void addHateByArticleId(String articleID) {
        logger.info("ActionLog.addHateByArticleId.start");
        int userId = Integer.parseInt((String) getAuthenticatedObject().getPrincipal());
        ArticleEntity articleEntity = articleRepository.findById(articleID)
                .orElseThrow(() -> new NoSuchArticleException("Article doesn't exist"));
        int articleUserId = articleEntity.getUserId();
        if (userId != articleUserId && getAuthenticatedObject().isAuthenticated()) {
            articleEntity.setHateCount(articleEntity.getHateCount() + 1);
            articleRepository.save(articleEntity);
        } else {
            logger.error("Thrown.NoAccessException");
            throw new NoAccessException("You don't have access to hate");
        }
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.error("Thrown.NotValidTokenException");
            throw new NotValidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
