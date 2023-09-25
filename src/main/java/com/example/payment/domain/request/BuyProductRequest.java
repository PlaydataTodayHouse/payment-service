package com.example.payment.domain.request;

import com.example.payment.config.kafka.OrderKafkaData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BuyProductRequest {

    private Long productId;
    private String productName;
    private Integer productPrice;
    private Integer totalProductBuyQuantity;
    private List<BuyOptionDetailRequest> buyOptionDetails;

    public OrderKafkaData toOrderKafkaData() {
        return new OrderKafkaData(
                this.productId,
                this.getBuyOptionDetails().stream()
                        .map(BuyOptionDetailRequest::getOptionDetailId)
                        .toList()
        );
    }

}
