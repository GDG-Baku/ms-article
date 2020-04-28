package az.gdg.msarticle.service.impl;

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

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    @Override
    public void addReadCount(String articleId) {
        logger.info("ActionLog.add read count.start : articleId {}", articleId);
        Optional<ArticleEntity> articleEntity = articleRepository.findById(articleId);

        if (articleEntity.isPresent()) {
            ArticleEntity article = articleEntity.get();
            Integer count = article.getReadCount();
            article.setReadCount(count + 1);

            articleRepository.save(article);
        } else {
            logger.info("Thrown.ArticleNotFoundException");
            throw new ArticleNotFoundException("No found such article");
        }

        logger.info("ActionLog.add read count.stop.success");
    }
}
