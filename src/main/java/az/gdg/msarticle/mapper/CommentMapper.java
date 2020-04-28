package az.gdg.msarticle.mapper;

import az.gdg.msarticle.model.dto.CommentDTO;
import az.gdg.msarticle.model.dto.UserDTO;
import az.gdg.msarticle.model.entity.Comment;
import az.gdg.msarticle.service.MsAuthService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {
    private final MsAuthService msAuthService;

    public CommentMapper(MsAuthService msAuthService) {
        this.msAuthService = msAuthService;
    }

    public CommentDTO mapEntityToDto(Comment comment){
        UserDTO userDTO = msAuthService.getUserById(comment.getUserId());
        return CommentDTO.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .imageUrl(userDTO.getImageUrl())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .replies(comment.getReplies())
                .build();
    }

    public List<CommentDTO> mapEntityListToDtoList(List<Comment> comments){
        return comments.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
}