package az.gdg.msarticle.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private String id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Integer quackCount;
    private Integer hateCount;
    private Integer readCount;
    private String type;
    private boolean isDraft;
    private List<TagDTO> tags;
    private List<CommentDTO> comments;
}
