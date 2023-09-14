package com.example.payment.repository;

import com.example.payment.domain.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserUUID(UUID userUUID);
}
