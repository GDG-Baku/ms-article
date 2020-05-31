package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.client.MsAuthClient;
import az.gdg.msarticle.exception.ArticleNotFoundException;
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
    private final MsAuthClient msAuthClient;

    public ArticleServiceImpl(ArticleRepository articleRepository, MsAuthClient msAuthClient) {
        this.articleRepository = articleRepository;
        this.msAuthClient = msAuthClient;
    }


    @Override
    public void addReadCount(String articleId) {
        logger.info("ActionLog.addReadCount.start.articleId : {}", articleId);
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Not found such article"));

        Integer userId = articleEntity.getUserId();
        Integer count = articleEntity.getReadCount();
        articleEntity.setReadCount(count + 1);

        msAuthClient.addPopularity(userId);
        articleRepository.save(articleEntity);

        logger.info("ActionLog.addReadCount.stop.success");
    }
}
