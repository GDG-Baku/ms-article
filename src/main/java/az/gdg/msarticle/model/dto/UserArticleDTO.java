package az.gdg.msarticle.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserArticleDTO {
    private UserDTO userDTO;
    private List<ArticleDTO> articleDTOs;
}
