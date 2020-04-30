package az.gdg.msarticle.model;

import lombok.Data;

@Data
public class CommentRequest {
    private String id;
    private String articleId;
    private String text;
    private String parentCommentId;
}
