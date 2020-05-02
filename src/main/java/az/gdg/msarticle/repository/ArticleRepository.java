package az.gdg.msarticle.repository;

import az.gdg.msarticle.model.entity.ArticleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends MongoRepository<ArticleEntity,String> {

    List<ArticleEntity> getArticleEntitiesByUserId(Integer userId);

    List<ArticleEntity> getArticleEntitiesByUserIdAndIsDraftAndIsApproved(Integer userId, boolean isDraft,
                                                                          boolean isApproved);
}
