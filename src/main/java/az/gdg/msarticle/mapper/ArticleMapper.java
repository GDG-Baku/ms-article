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

    static Integer getValueOfType(String type) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.name().equalsIgnoreCase(type)) {
                return typeEnum.getValue();
            }
        }
        throw new TypeNotFoundException("Please, specify valid type");
    }

    @Named("getTypeOfValue")
    static String getTypeOfValue(Integer type) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.getValue() == type) {
                return typeEnum.name();
            }
        }
        throw new TypeNotFoundException("Please, specify valid type");
    }


    @Mapping(source = "type", target = "type", qualifiedByName = "getTypeOfValue")
    ArticleDTO entityToDto(ArticleEntity articleEntity);

    List<ArticleDTO> entityListToDtoList(List<ArticleEntity> articleEntity);

}
