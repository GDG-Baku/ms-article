package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.NoSuchArticleException;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;


    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    @Override
    public Integer getQuacksByArticleId(String articleID) {
        logger.info("ActionLog.getQuacksByArticleId.start");
        ArticleEntity articleEntity = articleRepository.findById(articleID)
                .orElseThrow(() -> new NoSuchArticleException("Article doesn't exist"));
        return articleEntity.getQuackCount();
    }

}
