package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.ArticleNotFoundException;
import az.gdg.msarticle.exception.NoAccessException;
import az.gdg.msarticle.exception.NotValidTokenException;
import az.gdg.msarticle.mail.service.EmailService;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
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
    private final EmailService emailService;
    private static final String NO_ACCESS_TO_REQUEST = "You don't have access for this request";

    public ArticleServiceImpl(ArticleRepository articleRepository, EmailService emailService) {
        this.articleRepository = articleRepository;
        this.emailService = emailService;
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new NotValidTokenException("Token is not valid or it is expired");
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

        if (articleEntity.getUserId() == Integer.parseInt(userId)) {
            if (articleEntity.isDraft()) {
                sendMail(articleId, "publish");
                logger.info("ActionLog.publishArticle.success");
                message = "Article is sent for reviewing";
            } else {
                message = "Article is already published";
            }
            return message;
        }
        logger.info("ActionLog.publishArticle.end");
        throw new NoAccessException(NO_ACCESS_TO_REQUEST);
    }


    private void sendMail(String articleId, String requestType) {
        logger.info("ActionLog.sendMail.start");
        String mailBody = "Author that has article with id " + articleId + " wants to " + requestType + " it.<br>" +
                "Please review article before " + requestType;
        emailService.sendToQueue(emailService.prepareMail(mailBody));
        logger.info("ActionLog.sendMail.end");
    }
}
