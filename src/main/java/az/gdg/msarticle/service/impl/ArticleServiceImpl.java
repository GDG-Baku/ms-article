package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.ArticleNotFoundException;
import az.gdg.msarticle.exception.NoAccessException;
import az.gdg.msarticle.exception.TypeNotFoundException;
import az.gdg.msarticle.model.ArticleRequest;
import az.gdg.msarticle.model.TypeEnum;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.service.ArticleService;
import az.gdg.msarticle.service.MailService;
import az.gdg.msarticle.service.TagService;
import az.gdg.msarticle.util.AuthUtil;
import az.gdg.msarticle.util.MailUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final MailService mailService;
    private final TagService tagService;
    private static final String NO_ACCESS_TO_REQUEST = "You don't have access for this request";

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              MailService mailService, TagService tagService) {
        this.articleRepository = articleRepository;
        this.mailService = mailService;
        this.tagService = tagService;
    }

    @Override
    public String updateArticle(String articleId, ArticleRequest articleRequest) {
        logger.info("ActionLog.updateArticle.start with id {}", articleId);
        String userId = (String) AuthUtil.getAuthenticatedObject().getPrincipal();

        ArticleEntity articleEntity = articleRepository.findById(articleId).orElseThrow(() ->
                new ArticleNotFoundException("Article doesn't exist with this id " + articleId));

        String message;

        if (articleEntity.getUserId() == Integer.parseInt(userId)) {
            articleRepository.save(buildEntityFromRequest(articleEntity, articleRequest));
            MailUtil.sendMail(articleId, "update", mailService);
            message = "Article is sent for reviewing";
            logger.info("ActionLog.updateArticle.success with id {}", articleId);
        } else {
            throw new NoAccessException(NO_ACCESS_TO_REQUEST);
        }
        logger.info("ActionLog.updateArticle.end with id {}", articleId);
        return message;
    }

    private ArticleEntity buildEntityFromRequest(ArticleEntity articleEntity,
                                                 ArticleRequest articleRequest) {
        articleEntity.setType(getValueOfType(articleRequest.getType()));
        articleEntity.setTitle(articleRequest.getTitle());
        articleEntity.setContent(articleRequest.getContent());
        articleEntity.setTags(tagService.getTagsFromRequest(articleRequest.getTags()));
        articleEntity.setDraft(true);
        return articleEntity;
    }

    private Integer getValueOfType(String type) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.name().equalsIgnoreCase(type)) {
                return typeEnum.getValue();
            }
        }
        throw new TypeNotFoundException("Please, specify valid type");
    }
}
