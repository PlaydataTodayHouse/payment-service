package com.example.payment.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCartRequest {
    private UUID userUUID;
    private Long productId;
    private int quantity;
}
