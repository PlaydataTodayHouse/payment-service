package com.example.payment.config.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;


@Builder
@AllArgsConstructor
@Getter
public class TokenInfo {
    private UUID id;
    private String userId;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private LocalDate birth;
    private String role;
}
