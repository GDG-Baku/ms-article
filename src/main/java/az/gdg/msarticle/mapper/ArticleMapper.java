package az.gdg.msarticle.mapper;

import az.gdg.msarticle.exception.InvalidTypeException;
import az.gdg.msarticle.exception.TypeNotFoundException;
import az.gdg.msarticle.model.ArticleRequest;
import az.gdg.msarticle.model.TypeEnum;
import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.model.dto.UserDTO;
import az.gdg.msarticle.model.entity.ArticleEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {
    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    @Named("getValueOfType")
    static Integer getValueOfType(String type) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.name().equalsIgnoreCase(type)) {
                return typeEnum.getValue();
            }
        }
        throw new InvalidTypeException("Please, specify valid type");
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

    @Mapping(source = "type", target = "type", qualifiedByName = "getValueOfType")
    ArticleEntity requestToEntity(ArticleRequest articleRequest);

    ArticleEntity dtoToEntity(ArticleDTO articleDTO);

    List<ArticleDTO> entityToDtoList(List<ArticleEntity> articleEntities);

    @Mapping(target = "comments", ignore = true)
    @Mapping(source = "articleEntity.type", target = "type", qualifiedByName = "getTypeOfValue")
    @Mapping(source = "userDTO", target = "userDTO")
    ArticleDTO entityToDto(ArticleEntity articleEntity, UserDTO userDTO);
}
