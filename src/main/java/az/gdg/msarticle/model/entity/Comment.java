package az.gdg.msarticle.model.entity;

import az.gdg.msarticle.model.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Integer userId;
    private String text;
    private List<CommentDTO> replies;
    private LocalDateTime createdAt;
}
