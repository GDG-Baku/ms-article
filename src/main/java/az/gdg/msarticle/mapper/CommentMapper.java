package az.gdg.msarticle.mapper;

import az.gdg.msarticle.model.CommentRequest;
import az.gdg.msarticle.model.dto.CommentDTO;
import az.gdg.msarticle.model.dto.UserDTO;
import az.gdg.msarticle.model.entity.CommentEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentEntity requestToEntity(CommentRequest commentRequest);

    List<CommentDTO> entityToDtoList(List<CommentEntity> commentEntity);

    CommentDTO entityToDto(CommentEntity commentEntity, UserDTO userDTO);
}


