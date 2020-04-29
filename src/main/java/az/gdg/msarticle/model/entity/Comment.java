package az.gdg.msarticle.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    private String id;
    private String articleId;
    private String text;
    private Integer userId;
    private String parentCommentId;
    private LocalDateTime createdAt;
}
