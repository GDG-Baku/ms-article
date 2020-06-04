package az.gdg.msarticle.model.client.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailDTO {
    private List<String> to;
    private String subject;
    private String body;
}