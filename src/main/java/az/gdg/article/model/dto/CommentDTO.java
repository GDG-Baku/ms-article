package az.gdg.article.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String text;
    private LocalDateTime createdAt;
    private List<CommentDTO> replies;
}
