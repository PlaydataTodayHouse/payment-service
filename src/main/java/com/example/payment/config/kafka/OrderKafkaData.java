package com.example.payment.config.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderKafkaData {

    private Long productId;
    private List<Long> optionDetailId;

}
