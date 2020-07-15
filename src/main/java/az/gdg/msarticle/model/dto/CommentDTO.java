package az.gdg.msarticle.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private String id;
    private Long userId;
    private UserDTO userDTO;
    private String text;
    private LocalDateTime createdAt;
    private List<CommentDTO> replies;
}
