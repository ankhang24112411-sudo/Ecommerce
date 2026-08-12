package com.khang.backendecommerce.newstruc.service.impl;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
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


    @Value("${sendgrid.from-email}")
    private String from;

    @Value("${sendgrid.template-id}")
    private String templateId;



    @Value("${sendgrid.order-status-template-id}")
    private String orderStatusTemplateId;

    public void sendOrderStatusMail(SubOrderStatusEvent event) {
        Mail mail = new Mail();

        mail.setFrom(new Email(from, "Khang Store"));
        mail.setTemplateId(orderStatusTemplateId);

        Personalization personalization = new Personalization();

        personalization.addTo(
                new Email(event.customerEmail())
        );

        // DATA CÓ SẴN TRONG EVENT
        personalization.addDynamicTemplateData(
                "orderId",
                event.orderId()
        );

        personalization.addDynamicTemplateData(
                "orderCode",
                event.orderCode()
        );

        personalization.addDynamicTemplateData(
                "subOrderId",
                event.subOrderId()
        );

        personalization.addDynamicTemplateData(
                "subOrderCode",
                event.subOrderCode()
        );

        personalization.addDynamicTemplateData(
                "trackingCode",
                event.trackingCode()
        );

        personalization.addDynamicTemplateData(
                "customerName",
                event.customerName()
        );

        personalization.addDynamicTemplateData(
                "customerEmail",
                event.customerEmail()
        );

        personalization.addDynamicTemplateData(
                "status",
                event.status().name()
        );


        // DATA CHO GIAO DIỆN MAIL
        if (event.status() == OrderStatus.SHIPPING) {

            personalization.addDynamicTemplateData(
                    "subject",
                    "Đơn hàng đang được giao"
            );

            personalization.addDynamicTemplateData(
                    "title",
                    "Đơn hàng đang trên đường đến bạn"
            );

            personalization.addDynamicTemplateData(
                    "message",
                    "Shipper đã lấy hàng và đang giao đơn hàng đến bạn."
            );
        }

        if (event.status() == OrderStatus.DELIVERED) {

            personalization.addDynamicTemplateData(
                    "subject",
                    "Giao hàng thành công"
            );

            personalization.addDynamicTemplateData(
                    "title",
                    "Đơn hàng đã được giao thành công"
            );

            personalization.addDynamicTemplateData(
                    "message",
                    "Cảm ơn bạn đã mua hàng tại Khang Store."
            );
        }

        mail.addPersonalization(personalization);

        send(mail);
    }


    private void send(Mail mail) {

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            if (response.getStatusCode() == 202) {
                log.info("Email sent successfully");
            } else {
                log.error(
                        "Email failed - status: {}, body: {}",
                        response.getStatusCode(),
                        response.getBody()
                );
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }


