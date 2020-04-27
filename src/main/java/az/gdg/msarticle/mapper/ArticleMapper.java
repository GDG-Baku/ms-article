package az.gdg.msarticle.mapper;

import az.gdg.msarticle.model.ArticleRequest;
import az.gdg.msarticle.model.entity.ArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {
    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    ArticleEntity requestToEntity(ArticleRequest articleRequest);

}
