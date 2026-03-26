package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private double totalPrice;

    private String status; // PENDING, PAID

    private String address;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;

    private String stripeSessionId;
}