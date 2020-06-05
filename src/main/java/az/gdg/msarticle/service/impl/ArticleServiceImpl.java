package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.ArticleNotFoundException;
import az.gdg.msarticle.exception.InvalidTokenException;
import az.gdg.msarticle.exception.NoAccessException;
import az.gdg.msarticle.exception.NoDraftedArticleExist;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.service.ArticleService;
import az.gdg.msarticle.service.MailService;
import az.gdg.msarticle.util.MailUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final MailService mailService;
    private static final String NO_ACCESS_TO_REQUEST = "You don't have access for this request";

    public ArticleServiceImpl(ArticleRepository articleRepository, MailService mailService) {
        this.articleRepository = articleRepository;
        this.mailService = mailService;
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new InvalidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String publishArticle(String articleId) {
        logger.info("ActionLog.publishArticle.start with articleId {}", articleId);
        String userId = (String) getAuthenticatedObject().getPrincipal();
        String message;
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article doesn't exist with this id" + articleId));

        if (String.valueOf(articleEntity.getUserId()).equals(userId)) {
            if (articleEntity.isDraft()) {
                MailUtil.sendMail(articleId, "publish", mailService);
                logger.info("ActionLog.publishArticle.success");
                message = "Article is sent for reviewing";
            } else {
                throw new NoDraftedArticleExist("Article is already published");
            }
            return message;
        }
        logger.info("ActionLog.publishArticle.end");
        throw new NoAccessException(NO_ACCESS_TO_REQUEST);
    }
}
