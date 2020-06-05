package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.InvalidTokenException;
import az.gdg.msarticle.mapper.ArticleMapper;
import az.gdg.msarticle.model.ArticleRequest;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.service.ArticleService;
import az.gdg.msarticle.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final TagService tagService;

    public ArticleServiceImpl(ArticleRepository articleRepository, TagService tagService) {
        this.articleRepository = articleRepository;
        this.tagService = tagService;
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new InvalidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String addDraft(String token, ArticleRequest articleRequest) {
        logger.info("ActionLog.addDraft.start");
        String userId = (String) getAuthenticatedObject().getPrincipal();

        ArticleEntity draft = ArticleMapper.INSTANCE.requestToEntity(articleRequest);
        draft.setUserId(Integer.parseInt(userId));
        draft.setDraft(true);
        draft.setReadCount(0);
        draft.setHateCount(0);
        draft.setQuackCount(0);
        draft.setApproved(false);
        draft.setApproverId(null);
        draft.setComments(Collections.emptyList());

        if (articleRequest.getType().equals("NEWS")) {
            draft.setTags(Collections.emptyList());
        } else {
            draft.setTags(tagService.getTagsFromRequest(articleRequest.getTags()));
        }

        articleRepository.save(draft);

        logger.info("ActionLog.addDraft.stop.success");

        return draft.getId();


    }
}
