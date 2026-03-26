package com.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.entity.CartItem;
import com.ecommerce.service.CartService;

import java.util.List;
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @PostMapping("/add")
    public CartItem add(@RequestParam Long productId,
                        @RequestParam int qty) {
        return service.add(productId, qty);
    }

    @GetMapping
    public List<CartItem> getCart() {
        return service.getUserCart();
    }
}