package com.example.payment.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PurchaseCartResponse {
    private UUID orderId;
    private LocalDateTime orderTime;
}