package com.example.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Product {
    private Long id;

    private String categoryId;
    private String name;
    private String brand;
    private String defaultColor;
    private String deliveryType;
    private double deliveryFee;
    private int likesCount;
    private String descriptionPost;
    private int quantity;
}
