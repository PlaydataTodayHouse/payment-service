package com.example.payment.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PurchaseRequest {

    private Integer totalPrice;
    private List<BuyProductRequest> buyProducts;

}
