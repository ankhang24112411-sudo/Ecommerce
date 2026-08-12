package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.newstruc.dto.event.SubOrderStatusEvent;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Personalization;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.sendgrid.helpers.mail.objects.Email;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final SendGrid sendGrid;

    @Value("${sendgrid.api-key}")
    private String apiKey;

    @Value("${sendgrid.from-email}")
    private String from;

    @Value("${sendgrid.template-id}")
    private String templateId;

    @Value("${sendgrid.verification-link}")
    private String verificationLink;
    public void sendOrderStatusMail(SubOrderStatusEvent event) {
//
//        Email fromEmail = new Email(from, "Khang Store");
//        Email toEmail = new Email(event.customerEmail());
//
//        Mail mail = new Mail();
//        mail.setFrom(fromEmail);
//        mail.setTemplateId(templateId);
//
//        Personalization personalization = new Personalization();
//        personalization.addTo(toEmail);
//
//        personalization.addDynamicTemplateData(
//                "customerName",
//                event.customerName()
//        );
//
//        personalization.addDynamicTemplateData(
//                "orderCode",
//                event.orderCode()
//        );
//
//        personalization.addDynamicTemplateData(
//                "subOrderCode",
//                event.subOrderCode()
//        );
//
//        personalization.addDynamicTemplateData(
//                "trackingCode",
//                event.trackingCode()
//        );
//
//
//
//
//        personalization.addDynamicTemplateData(
//                "status",
//                event.status().name()
//        );
//
//        personalization.addDynamicTemplateData(
//                "updatedAt",
//                event.updatedAt().toString()
//        );
//
//        // Nội dung thay đổi theo status
//        if (event.status() == SubOrderStatus.SHIPPING) {
//
//            personalization.addDynamicTemplateData(
//                    "title",
//                    "Đơn hàng đang trên đường giao"
//            );
//
//            personalization.addDynamicTemplateData(
//                    "message",
//                    "Shipper đã nhận hàng và đang giao đến bạn."
//            );
//        }
//
//        if (event.status() == SubOrderStatus.DELIVERED) {
//
//            personalization.addDynamicTemplateData(
//                    "title",
//                    "Giao hàng thành công"
//            );
//
//            personalization.addDynamicTemplateData(
//                    "message",
//                    "Cảm ơn bạn đã mua hàng tại Khang Store."
//            );
//        }
//
//        mail.addPersonalization(personalization);
//
//        send(mail);
//    }
    }
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
            if(response.getStatusCode() == 202){
                log.info("Email send successfully");
            }
            else{
                log.error("Email send failed");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//    public void sendVerificationEmail(String to, String name) throws IOException {
//        log.info("Sending verification email for name={}", name);
//
//        Email fromEmail = new Email(from, "Khang Nguyen");
//        Email toEmail = new Email(to);
//        String subject = "Xác thực tài khoản";
//
//        // Generate secret code and save to db
//        String secretCode = UUID.randomUUID().toString();
//        log.info("secretCode = {}", secretCode);
//
//        // TOD0 save secretCode to db
//
//        // Tạo dynamic template data
//        Map<String, String> dynamicTemplateData = new HashMap<>();
//        dynamicTemplateData.put("name", name);
//        dynamicTemplateData.put("verification_link", verificationLink + "?secretCode=" + secretCode);
//
//        Mail mail = new Mail();
//        mail.setFrom(fromEmail);
//        mail.setSubject(subject);
//        Personalization personalization = new Personalization();
//        personalization.addTo(toEmail);
//
//        // Add dynamic template data
//        dynamicTemplateData.forEach(personalization::addDynamicTemplateData);
//
//        mail.addPersonalization(personalization);
//        mail.setTemplateId(templateId); // Template ID từ SendGrid
//
//        Request request = new Request();
//        request.setMethod(Method.POST);
//        request.setEndpoint("mail/send");
//        request.setBody(mail.build());
//        Response response = sendGrid.api(request);
//        if (response.getStatusCode() == 202) {
//            log.info("Verification sent successfully");
//        } else {
//            log.error("Verification sent failed");
//        }
//    }

}