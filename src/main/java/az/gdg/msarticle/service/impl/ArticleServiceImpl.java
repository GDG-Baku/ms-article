package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.client.MsAuthClient;
import az.gdg.msarticle.exception.ArticleNotFoundException;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        logger.info("ActionLog.addReadCount.start : articleId {}", articleId);
        Optional<ArticleEntity> articleEntity = articleRepository.findById(articleId);

        if (articleEntity.isPresent()) {
            ArticleEntity article = articleEntity.get();

            Integer userId = article.getUserId();
            Integer count = article.getReadCount();
            article.setReadCount(count + 1);

            msAuthClient.addPopularity(userId);
            articleRepository.save(article);
        } else {
            throw new ArticleNotFoundException("Not found such article");
        }

        logger.info("ActionLog.addReadCount.stop.success");
    }
}
