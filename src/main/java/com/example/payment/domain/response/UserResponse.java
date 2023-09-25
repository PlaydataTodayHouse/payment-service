package com.example.payment.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private LocalDate birth;
    private String profileImage;
    private String role;

}
