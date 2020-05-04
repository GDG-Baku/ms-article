package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.InvalidTokenException;
import az.gdg.msarticle.mapper.ArticleMapper;
import az.gdg.msarticle.model.ArticleRequest;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.repository.TagRepository;
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
    private final TagRepository tagRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new InvalidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String addDraft(String token, ArticleRequest articleRequest) {
        logger.info("ActionLog.addDraft.Start : token {}", token);
        String userId = (String) getAuthenticatedObject().getPrincipal();

        ArticleEntity draft = ArticleMapper.INSTANCE.requestToEntity(articleRequest);
        draft.setUserId(Integer.parseInt(userId));
        draft.setDraft(true);
        draft.setReadCount(0);
        draft.setHateCount(0);
        draft.setQuackCount(0);
        draft.setApproved(false);
        draft.setApproverId(null);
        draft.setComments(null);
        tagRepository.saveAll(draft.getTags());
        articleRepository.save(draft);

        logger.info("ActionLog.addDraft.Stop.Success");

        return draft.getId();

    }
}
