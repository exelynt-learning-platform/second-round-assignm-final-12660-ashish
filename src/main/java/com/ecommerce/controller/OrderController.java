package com.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.entity.Order;
import com.ecommerce.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public Order placeOrder(@RequestParam String address) {
        return service.placeOrder(address);
    }

    @GetMapping
    public List<Order> getOrders() {
        return service.getUserOrders();
    }
}