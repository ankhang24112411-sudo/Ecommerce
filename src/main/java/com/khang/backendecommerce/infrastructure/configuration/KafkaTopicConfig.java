package com.khang.backendecommerce.infrastructure.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class KafkaTopicConfig {
    @Bean
    public NewTopic subOrderStatusTopic() {
        return new NewTopic(KafkaTopics.SUBORDER_STATUS, 3, (short) 1);
    }
}
