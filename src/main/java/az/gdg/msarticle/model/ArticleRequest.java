package az.gdg.msarticle.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleRequest {

    private String title;
    private String content;
    private List<TagRequest> tags;
    private String type;
}
