package az.gdg.article.repository;

import az.gdg.article.model.entity.TagEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends MongoRepository<TagEntity, String> {
}
