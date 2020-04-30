package az.gdg.msarticle.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comment")
public class CommentEntity {
    @Id
    private String id;
    private String text;
    private Integer userId;
    private boolean isReply;
    @DBRef
    private List<CommentEntity> replies;
    private LocalDateTime createdAt;
}
