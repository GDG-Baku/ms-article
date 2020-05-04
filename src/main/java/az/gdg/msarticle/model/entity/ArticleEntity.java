package az.gdg.msarticle.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "articles")
public class ArticleEntity {
    @Id
    private String id;
    private Integer userId;
    private String title;
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
    private Integer quackCount;
    private Integer hateCount;
    private Integer readCount;
    private boolean isDraft;
    private boolean isApproved;
    private Integer approverId;
    @DBRef
    private List<TagEntity> tags;
    @DBRef
    private List<CommentEntity> comments;
}