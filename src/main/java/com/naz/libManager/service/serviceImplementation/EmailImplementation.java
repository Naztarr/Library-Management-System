package com.naz.libManager.service.serviceImplementation;

import com.naz.libManager.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailImplementation implements EmailService {
    @Autowired
    private final JavaMailSender mailSender;

    public EmailImplementation(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Value("${MAIL_USERNAME}")
    private String sender;

    /**
     * Sends an email message with the provided content, subject, and recipient.
     *
     * @param message   The content of the email message.
     * @param subject   The subject of the email message.
     * @param recipient The email address of the recipient.
     */
    @SneakyThrows
    @Override
    public void sendMail(String message, String subject, String recipient) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setSubject(subject);
        messageHelper.setFrom(sender);
        messageHelper.setTo(recipient);
        messageHelper.setText(message, true);
        messageHelper.setSentDate(new Date(System.currentTimeMillis()));
        mailSender.send(mimeMessage);

    }
}
