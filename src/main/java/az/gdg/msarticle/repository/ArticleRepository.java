package az.gdg.msarticle.repository;

import az.gdg.msarticle.model.entity.ArticleEntity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<ArticleEntity, String> {
}
