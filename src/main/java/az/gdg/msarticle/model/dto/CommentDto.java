package az.gdg.msarticle.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String index;
    private String text;
    private Integer userId;
    private LocalDateTime createdAt;
    private List<CommentDto> replies;
}
