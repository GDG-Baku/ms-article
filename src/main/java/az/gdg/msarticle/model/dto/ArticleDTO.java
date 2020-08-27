package az.gdg.msarticle.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private String id;
    private UserDTO userDTO;
    private String type;
    private Integer userId;
    private String title;
    private String content;
    private Integer quackCount;
    private Integer readCount;
    private Integer hateCount;
    private LocalDateTime createdAt;
    private boolean isDraft;
    private List<TagDTO> tags;
    private List<CommentDTO> comments;
}
