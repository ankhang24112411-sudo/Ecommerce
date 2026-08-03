package com.khang.backendecommerce.newstruc.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import com.sendgrid.helpers.mail.objects.Email;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final SendGrid sendGrid;

    @Value("${spring.sendGrid.fromEmail:dummy-from-email}")
    private String from;

    @Value("${spring.sendGrid.templateId:dummy-template-id}")
    private String templateId;

    @Value("${spring.sendGrid.verificationLink:dummy-verification-link}")
    private String verificationLink;
    public void send(String to, String subject , String text)  {
        Email fromEmail = new Email(from);
        Email toEmail = new Email(to);

        Content content = new Content("text/plain", text);
        Mail mail = new Mail(fromEmail, subject, toEmail,content);

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = new SendGrid(from).api(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}