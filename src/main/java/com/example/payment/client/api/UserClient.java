package com.example.payment.client.api;

import com.example.payment.domain.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient("AUTH-SERVICE")
public interface UserClient {
    @GetMapping("/api/v1/auth/findUser/{userUUID}")
    UserResponse getByUUID(@PathVariable UUID userUUID);
}
