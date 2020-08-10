package az.gdg.msarticle.repository;

import az.gdg.msarticle.model.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends MongoRepository<ArticleEntity, String> {

    List<ArticleEntity> getArticleEntitiesByUserId(Integer userId);

    List<ArticleEntity> getArticleEntitiesByUserIdAndIsDraft(Integer userId, boolean isDraft);

    Page<ArticleEntity> getArticleEntitiesByUserId(Long userId, Pageable pageable);

    Page<ArticleEntity> getArticleEntitiesByUserIdAndIsDraftFalseAndIsApprovedTrue(Long userId, Pageable pageable);
}
