package az.gdg.msarticle.model.dto;

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
public class ArticleDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String imageUrl;
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
