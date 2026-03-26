package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;   // 👈 NEW (IMPORTANT)

    @ManyToOne
    private Product product;

    private int quantity;
}