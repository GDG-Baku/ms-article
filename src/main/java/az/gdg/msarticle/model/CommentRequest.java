package az.gdg.msarticle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private Integer index;
    private String articleId;
    private String text;
    private Integer userId;
    private Integer parentCommentIndex;
    private LocalDateTime createdAt;
}
