package com.example.payment.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BuyOptionDetailRequest {

    private Long optionDetailId;
    private String optionDetailName;
    private Integer optionDetailPrice;
    private Integer optionDetailBuyQuantity;

}
