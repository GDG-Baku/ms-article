package az.gdg.msarticle.repository;

import az.gdg.msarticle.model.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends MongoRepository<ArticleEntity, String> {

    Page<ArticleEntity> getArticleEntitiesByUserId(Integer userId, Pageable pageable);

    Page<ArticleEntity> getArticleEntitiesByUserIdAndIsDraftFalseAndIsApprovedTrue(Integer userId, Pageable pageable);
}
