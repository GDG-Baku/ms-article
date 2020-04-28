package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.ArticleNotFound;
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

    /*@Override
    public String createDraft(ArticleRequest articleRequest) {
        ArticleEntity articleEntity = ArticleMapper.INSTANCE.requestToEntity(articleRequest);
        articleEntity.setDraft(true);
        articleEntity.setUserId(31);
        articleRepository.save(articleEntity);
        return "Article is drafted";
    }*/

    @Override
    public String publishArticle(String articleId) {
        logger.info("ActionLog.publishArticle.start with articleId {}", articleId);
        String userId = (String) getAuthenticatedObject().getPrincipal();
        String message;
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFound("Article doesn't exist with this id" + articleId));

        if (articleEntity.getUserId() == Integer.parseInt(userId)) {
            if (articleEntity.isDraft()) {
                sendMail(articleId);
                logger.info("ActionLog.publishArticle.success");
                message = "Article is sent for reviewing";
            } else {
                message = "Article is already published";
            }
            articleRepository.save(articleEntity);
            return message;
        }
        logger.info("ActionLog.publishArticle.end");
        return "You don't have permission for this";
    }


    private void sendMail(String articleId) {

        String mailBody = "Author that has article with id " + articleId + " wants to publish it.<br>" +
                "Please review article before publishing";
        emailService.sendToQueue(emailService.prepareMail(mailBody));
    }
}
