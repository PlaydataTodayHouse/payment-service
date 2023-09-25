package com.example.payment.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {

    public static final String ORDER_COMMAND = "ORDER-COMMAND";

    @Bean
    public NewTopic orderCommand() {
        return TopicBuilder
                .name(ORDER_COMMAND)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
