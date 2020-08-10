package az.gdg.msarticle.service;

import az.gdg.msarticle.model.client.mail.MailDTO;

public interface MailService {
    void sendToQueue(MailDTO mailDto);
}
