package az.gdg.msarticle.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserArticleDTO {
    private UserDTO userDTO;
    private List<ArticleDTO> articleDTOs;
}
