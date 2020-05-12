package az.gdg.msarticle.model;

import az.gdg.msarticle.model.dto.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleRequest {

    private String title;
    private String content;
    private List<TagDTO> tags;
    private String type;
}
