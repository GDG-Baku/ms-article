package az.gdg.msarticle.mail.service.impl;

import az.gdg.msarticle.mail.model.MailDTO;
import az.gdg.msarticle.mail.service.EmailService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(Source.class)
public class EmailServiceImpl implements EmailService {
    private final Source source;

    public EmailServiceImpl(Source source) {
        this.source = source;
    }

    @Override
    public void sendToQueue(MailDTO mailDto) {
        source.output().send(MessageBuilder.withPayload(mailDto).build());
    }

    @Override
    public MailDTO prepareMail(String mailBody) {
        List<String> receivers = new ArrayList<>();
        //receivers.add("gdg.rubber.duck@gmail.com");
        //receivers.add("movsum.nigar@gmail.com");
        receivers.add("asif.hajiyev@outlook.com");
        receivers.add("asifhaciyev1498@gmail.com");
        //receivers.add("huseynov_ali@outlook.com");
        //receivers.add("isgandarli_murad@mail.ru");

        return new MailDTO().builder()
                .to(receivers)
                .body("<h2>" + mailBody + "</h2>")
                .subject("ARTICLE REVIEW")
                .build();
    }
}
