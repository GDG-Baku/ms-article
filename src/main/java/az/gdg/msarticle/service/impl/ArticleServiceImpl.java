package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.ExceedLimitException;
import az.gdg.msarticle.exception.NoSuchArticleException;
import az.gdg.msarticle.exception.NotValidTokenException;
import az.gdg.msarticle.exception.UnauthorizedAccessException;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.service.ArticleService;
import az.gdg.msarticle.service.MsAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final MsAuthService msAuthService;


    public ArticleServiceImpl(ArticleRepository articleRepository, MsAuthService msAuthService) {
        this.articleRepository = articleRepository;
        this.msAuthService = msAuthService;
    }


    @Override
    public void addHateByArticleId(String articleID, String token) {
        logger.info("ActionLog.addHateByArticleId.start");
        Integer userId = Integer.parseInt((String) getAuthenticatedObject().getPrincipal());
        ArticleEntity articleEntity = articleRepository.findById(articleID)
                .orElseThrow(() -> new NoSuchArticleException("Article doesn't exist"));
        Integer articleUserId = articleEntity.getUserId();
        Integer remainingQuackCount = msAuthService.getRemainingHateCount(token);
        if (articleUserId.equals(userId) && getAuthenticatedObject().isAuthenticated()) {
            if (remainingQuackCount > 0) {
                articleEntity.setHateCount(articleEntity.getHateCount() + 1);
                articleRepository.save(articleEntity);
                msAuthService.updateRemainingHateCount(token);
            } else {
                logger.error("Thrown.ExceedLimitException");
                throw new ExceedLimitException("You've already used your daily hates");
            }
        } else {
            logger.error("Thrown.UnauthorizedAccessException");
            throw new UnauthorizedAccessException("You don't have access to hate");
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
