package az.gdg.msarticle.mapper;

import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.model.entity.ArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {

    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    ArticleEntity dtoToEntity(ArticleDTO articleDTO);

    @Mapping(target = "commentEntities", ignore = true)
    ArticleDTO entityToDto(ArticleEntity articleEntity);

    List<ArticleDTO> entityToDtoList(List<ArticleEntity> articleEntities);
}
