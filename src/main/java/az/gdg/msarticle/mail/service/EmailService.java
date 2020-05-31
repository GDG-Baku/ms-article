package az.gdg.msarticle.mail.service;

import az.gdg.msarticle.mail.model.MailDTO;

public interface EmailService {
    void sendToQueue(MailDTO mailDto);

    MailDTO prepareMail(String mailBody);
}
