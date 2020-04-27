package az.gdg.msarticle.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEntity {

    private String articleId;
    private String text;
    private Integer userId;
    private Integer parentCommentIndex;
    private LocalDateTime createdAt;

}
