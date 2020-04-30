package az.gdg.msarticle.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDTO {

    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Integer clapCount;
    private Integer readCount;
    private boolean isDraft;
    private List<TagDTO> tags;
    private List<CommentDTO> comments;
}
