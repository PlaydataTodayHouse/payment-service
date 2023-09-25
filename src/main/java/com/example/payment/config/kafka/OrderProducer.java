package com.example.payment.config.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, List<OrderKafkaData>> kafkaTemplate;

    public void send(List<OrderKafkaData> orderKafkaData) {
        CompletableFuture<SendResult<String, List<OrderKafkaData>>> result =
                kafkaTemplate.send(TopicConfig.ORDER_COMMAND, orderKafkaData);

        result.thenAccept(sendResult -> {
            System.out.println("Sent message=[" + orderKafkaData +
                    "] with offset=[" + sendResult.getRecordMetadata().offset() + "]");
        });
    }

}
