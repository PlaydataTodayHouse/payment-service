package com.example.payment.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseCartRequest {
    private String name;
    private String address;
    private String phoneNumber;
}