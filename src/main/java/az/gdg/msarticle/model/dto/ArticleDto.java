package az.gdg.msarticle.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private String articleId;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Integer clapCount;
    private Integer readCount;
    private boolean isDraft;
    private List<TagDto> tags;
    private List<CommentDto> comments;
}
