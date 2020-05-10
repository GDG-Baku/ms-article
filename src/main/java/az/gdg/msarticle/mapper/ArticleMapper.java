package az.gdg.msarticle.mapper;

import az.gdg.msarticle.exception.TypeNotFoundException;
import az.gdg.msarticle.model.TypeEnum;
import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.model.entity.ArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {

    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    ArticleEntity dtoToEntity(ArticleDTO articleDTO);

    @Named("getTypeOfValye")
    static String getTypeOfValye(Integer type) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.getValue() == type) {
                return typeEnum.name();
            }
        }
        throw new TypeNotFoundException("Please, specify valid type");
    }

    List<ArticleDTO> entityToDtoList(List<ArticleEntity> articleEntities);

    @Mapping(target = "comments", ignore = true)
    @Mapping(source = "type", target = "type", qualifiedByName = "getTypeOfValye")
    ArticleDTO entityToDto(ArticleEntity articleEntity);
}
