package az.gdg.msarticle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequest {
    private String title;
    private String content;
    private Integer clapCount;
    private Integer readCount;
    private List<TagRequest> tags;
    private List<CommentRequest> comments;
}
