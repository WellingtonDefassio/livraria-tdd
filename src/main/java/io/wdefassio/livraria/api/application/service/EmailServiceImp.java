package io.wdefassio.livraria.api.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImp implements EmailService {

    @Value("${mail.from}")
    private String from;
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmails(String message, List<String> emails) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setSubject("book loan late");
        mailMessage.setText(message);
        String[] mails = emails.toArray(new String[emails.size()]);
        mailMessage.setTo(mails);


        javaMailSender.send(mailMessage);

    }
}
