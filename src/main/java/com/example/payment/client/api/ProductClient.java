package com.example.payment.client.api;

import com.example.payment.domain.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("PRODUCT-SERVICE")
public interface ProductClient {
    @GetMapping("/api/v1/products/{productId}")
    Product getById(@PathVariable Long productId);
}
