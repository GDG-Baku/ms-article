package az.gdg.msarticle.util;

import az.gdg.msarticle.model.client.mail.MailDTO;
import az.gdg.msarticle.service.MailService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    private MailUtil() {
    }

    public static MailDTO buildMail(List<String> receivers, String mailBody) {
        return MailDTO.builder()
                .to(receivers)
                .body("<h2>" + mailBody + "</h2>")
                .subject("ARTICLE REVIEW")
                .build();
    }

    public static void sendMail(String articleId, String requestType,
                                MailService mailService, List<String> receivers) {
        logger.info("ServiceLog.sendMail.start");
        String mailBody = "Author that has article with id " + articleId + " wants to " + requestType + " it.<br>" +
                "Please review article before " + requestType;
        mailService.sendToQueue(buildMail(receivers, mailBody));
        logger.info("ServiceLog.sendMail.end");
    }
}
