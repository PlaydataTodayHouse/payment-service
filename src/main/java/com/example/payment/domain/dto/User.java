package com.example.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class User {
    private Long id;
    private String userId;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    private LocalDate birth;
    private int walletBalance;
}
