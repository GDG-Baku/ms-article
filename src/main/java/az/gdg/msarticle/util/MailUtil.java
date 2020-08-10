package az.gdg.msarticle.util;

import az.gdg.msarticle.model.client.mail.MailDTO;

import java.util.List;

public class MailUtil {
    private MailUtil() {
    }

    public static MailDTO buildMail(List<String> receivers, String mailBody) {
        return MailDTO.builder()
                .to(receivers)
                .body("<h2>" + mailBody + "</h2>")
                .subject("ARTICLE REVIEW")
                .build();
    }
}
