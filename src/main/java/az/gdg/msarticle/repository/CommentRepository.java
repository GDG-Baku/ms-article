package az.gdg.msarticle.repository;

import az.gdg.msarticle.model.entity.CommentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<CommentEntity, String> {

}
