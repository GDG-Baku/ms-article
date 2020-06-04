package az.gdg.msarticle.util;

import az.gdg.msarticle.model.client.mail.MailDTO;

import java.util.ArrayList;
import java.util.List;

public class MailUtil {
    private MailUtil() {
    }

    public static MailDTO buildMail(String mailBody) {
        List<String> receivers = new ArrayList<>();
        receivers.add("gdg.rubber.duck@gmail.com");
        receivers.add("movsum.nigar@gmail.com");
        receivers.add("asif.hajiyev@outlook.com");
        receivers.add("huseynov_ali@outlook.com");
        receivers.add("isgandarli_murad@mail.ru");

        return MailDTO.builder()
                .to(receivers)
                .body("<h2>" + mailBody + "</h2>")
                .subject("ARTICLE REVIEW")
                .build();
    }
}
