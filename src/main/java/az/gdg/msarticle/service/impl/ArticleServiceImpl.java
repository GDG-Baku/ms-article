package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.ArticleNotFoundException;
import az.gdg.msarticle.exception.InvalidTokenException;
import az.gdg.msarticle.exception.NoAccessException;
import az.gdg.msarticle.exception.TypeNotFoundException;
import az.gdg.msarticle.mapper.TagMapper;
import az.gdg.msarticle.model.ArticleRequest;
import az.gdg.msarticle.model.TagRequest;
import az.gdg.msarticle.model.TypeEnum;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.model.entity.TagEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.repository.TagRepository;
import az.gdg.msarticle.service.ArticleService;
import az.gdg.msarticle.service.MailService;
import az.gdg.msarticle.util.MailUtil;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final MailService mailService;
    private static final String NO_ACCESS_TO_REQUEST = "You don't have access for this request";

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              TagRepository tagRepository,
                              MailService mailService) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
        this.mailService = mailService;
    }

    @Override
    public String updateArticle(String articleId, ArticleRequest articleRequest) {
        logger.info("ActionLog.updateArticle.start with id {}", articleId);
        String userId = (String) getAuthenticatedObject().getPrincipal();

        ArticleEntity articleEntity = articleRepository.findById(articleId).orElseThrow(() ->
                new ArticleNotFoundException("Article doesn't exist with this id " + articleId));

        String message;

        if (articleEntity.getUserId() == Integer.parseInt(userId)) {
            articleRepository.save(buildEntityFromRequest(articleEntity, articleRequest));
            sendMail(articleId, "update");
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
        articleEntity.setTags(getTagsFromRequest(articleRequest.getTags()));
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

    private List<TagEntity> getTagsFromRequest(List<TagRequest> tagRequests) {
        logger.info("ActionLog.getTagsFromRequest.start");
        List<TagEntity> tags = new ArrayList<>();
        for (TagRequest tagRequest : tagRequests) {
            TagEntity tagEntity = tagRepository.findByName(tagRequest.getName());
            if (tagEntity == null) {
                tagEntity = tagRepository.save(TagMapper.INSTANCE.requestToEntity(tagRequest));
            }
            tags.add(tagEntity);
        }
        logger.info("ActionLog.getTagsFromRequest.end");
        return tags;
    }

    private void sendMail(String articleId, String requestType) {
        logger.info("ActionLog.sendMail.start");
        String mailBody = "Author that has article with id " + articleId + " wants to " + requestType + " it.<br>" +
                "Please review article before " + requestType;
        mailService.sendToQueue(MailUtil.buildMail(mailBody));
        logger.info("ActionLog.sendMail.end");
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new InvalidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
