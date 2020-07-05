package az.gdg.msarticle.mapper;

import az.gdg.msarticle.model.CommentRequest;
import az.gdg.msarticle.model.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentEntity requestToEntity(CommentRequest commentRequest);
}
