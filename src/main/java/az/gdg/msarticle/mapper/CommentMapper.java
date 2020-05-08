package az.gdg.msarticle.mapper;

import az.gdg.msarticle.model.dto.CommentDTO;
import az.gdg.msarticle.model.dto.UserDTO;
import az.gdg.msarticle.model.entity.CommentEntity;
import az.gdg.msarticle.service.MsAuthService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {
    private final MsAuthService msAuthService;

    public CommentMapper(MsAuthService msAuthService) {
        this.msAuthService = msAuthService;
    }

    public CommentDTO mapEntityToDto(CommentEntity commentEntity) {
        UserDTO userDTO = msAuthService.getUserById(commentEntity.getUserId());
        return CommentDTO.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .imageUrl(userDTO.getImageUrl())
                .userId(commentEntity.getUserId())
                .text(commentEntity.getText())
                .createdAt(commentEntity.getCreatedAt())
                .replies(mapEntityListToDtoList(commentEntity.getReplies()))
                .build();
    }

    public List<CommentDTO> mapEntityListToDtoList(List<CommentEntity> commentEntities) {
        if (commentEntities.size() != 0) {
            return commentEntities.stream()
                    .map(this::mapEntityToDto)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}