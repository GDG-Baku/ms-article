package az.gdg.article.service.impl;

import az.gdg.article.mapper.ArticleMapper;
import az.gdg.article.model.ArticleRequest;
import az.gdg.article.model.entity.ArticleEntity;
import az.gdg.article.repository.ArticleRepository;
import az.gdg.article.repository.TagRepository;
import az.gdg.article.security.util.TokenUtil;
import az.gdg.article.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final TokenUtil tokenUtil;
    private final TagRepository tagRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository, TokenUtil tokenUtil, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tokenUtil = tokenUtil;
        this.tagRepository = tagRepository;
    }


    @Override
    public String addDraft(String token, ArticleRequest articleRequest) {
        Integer userId = tokenUtil.getIdFromToken(token);
        logger.info("ActionLog.AddDraftArticle.Start : id {}", userId);

        ArticleEntity draft = ArticleMapper.INSTANCE.requestToEntity(articleRequest);
        draft.setUserId(userId);
        draft.setDraft(true);
        draft.setApproved(false);
        draft.setApproverId(null);

        tagRepository.saveAll(draft.getTags());
        articleRepository.save(draft);

        logger.info("ActionLog.AddDraftArticle.Stop.Success");

        return draft.getId();

    }
}
