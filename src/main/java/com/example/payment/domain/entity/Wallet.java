package com.example.payment.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Wallet {
    @Id
    private UUID userUUID;
    private int balance;


    public void addBalance(int amount) {
        this.balance += amount;
    }

    public void subtractBalance(int amount) {
        if (this.balance < amount) {
            throw new IllegalArgumentException("Insufficient balance!");
        }
        this.balance -= amount;
    }
}
