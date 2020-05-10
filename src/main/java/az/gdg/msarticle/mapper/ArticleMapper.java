package az.gdg.msarticle.mapper;

import az.gdg.msarticle.exception.TypeNotFoundException;
import az.gdg.msarticle.model.ArticleRequest;
import az.gdg.msarticle.model.TypeEnum;
import az.gdg.msarticle.model.entity.ArticleEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {
    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    @Mapping(target = "tags", ignore = true)
    @Mapping(source = "type", target = "type", qualifiedByName = "getValueOfType")
    ArticleEntity requestToEntity(ArticleRequest articleRequest);

    @Named("getValueOfType")
    default Integer getValueOfType(String type) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.name().equalsIgnoreCase(type)) {
                return typeEnum.getValue();
            }
        }
        throw new TypeNotFoundException("Please, specify valid type");
    }

}
