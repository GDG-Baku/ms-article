package az.gdg.msarticle.repository;

import az.gdg.msarticle.model.entity.TagEntity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagRepository extends MongoRepository<TagEntity, String> {
    TagEntity findByName(String name);
}
