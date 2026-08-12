package com.khang.backendecommerce.infrastructure.configuration;

import com.khang.backendecommerce.infrastructure.common.enums.OrderStatus;
import com.khang.backendecommerce.infrastructure.configuration.kafka.KafkaTopics;
import com.khang.backendecommerce.newstruc.dto.event.SubOrderStatusEvent;
import com.khang.backendecommerce.newstruc.service.impl.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubOrderMailConsumer {
    private final MailService mailService;

    @KafkaListener(topics = KafkaTopics.SUBORDER_STATUS, groupId = "suborder-mail-group")
    public void consume(SubOrderStatusEvent event) {

        if (event.status() != OrderStatus.SHIPPING && event.status() != OrderStatus.DELIVERED) {
            return;
        }
        mailService.sendOrderStatusMail(event);
    }
}

