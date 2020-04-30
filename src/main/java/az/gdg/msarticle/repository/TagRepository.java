package az.gdg.msarticle.repository;

import az.gdg.msarticle.model.entity.TagEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends MongoRepository<TagEntity, String> {
}
