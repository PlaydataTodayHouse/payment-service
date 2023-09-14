package com.example.payment.config;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    public TokenInfo parseAccessToken(String token) {
        Claims body = (Claims) Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parse(token)
                .getBody();

        LocalDate birthDate = LocalDate.parse(body.get("birth", String.class));

        TokenInfo info = TokenInfo.builder()
                .id(UUID.fromString(body.get("id", String.class)))
                .userId(body.get("userId", String.class))
                .name(body.get("name", String.class))
                .address(body.get("address", String.class))
                .phoneNumber(body.get("phoneNumber", String.class))
                .email(body.get("email", String.class))
                .birth(birthDate)
                .role(body.get("role", String.class))
                .build();
        return info;
    }
}
