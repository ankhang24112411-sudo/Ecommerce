package com.khang.backendecommerce.infrastructure.configuration.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void send(String topic, String key, Object event) {
        kafkaTemplate.send(topic, key, event);
    }
}
