package az.gdg.msarticle.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "articles")
public class ArticleEntity {
    @Id
    private String id;
    private Integer userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer clapCount;
    private Integer readCount;
    private boolean isDraft;
    private boolean isApproved;
    private Integer approverId;
    @DBRef
    private List<TagEntity> tags;
    private List<Comment> comments;
}
