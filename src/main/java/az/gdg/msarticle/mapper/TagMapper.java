package az.gdg.msarticle.mapper;

import az.gdg.msarticle.model.TagRequest;
import az.gdg.msarticle.model.entity.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagEntity requestToEntity(TagRequest tagRequest);
}
