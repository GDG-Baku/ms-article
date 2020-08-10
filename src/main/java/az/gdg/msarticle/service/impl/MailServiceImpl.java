package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.model.client.mail.MailDTO;
import az.gdg.msarticle.service.MailService;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(Source.class)
public class MailServiceImpl implements MailService {
    private final Source source;

    public MailServiceImpl(Source source) {
        this.source = source;
    }

    @Override
    public void sendToQueue(MailDTO mailDto) {
        source.output().send(MessageBuilder.withPayload(mailDto).build());
    }
}
