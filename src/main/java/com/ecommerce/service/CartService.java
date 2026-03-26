package com.ecommerce.service;

import com.ecommerce.config.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.ecommerce.entity.*;
import com.ecommerce.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository repo;
    private final ProductRepository productRepo;

    private User getCurrentUser() {
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return userDetails.getUser();
    }

    public CartItem add(Long productId, int qty) {

        User user = getCurrentUser();   // 👈 GET USER

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = new CartItem();
        item.setUser(user);   // 👈 LINK USER
        item.setProduct(product);
        item.setQuantity(qty);

        return repo.save(item);
    }

    public List<CartItem> getUserCart() {

        User user = getCurrentUser();   // 👈 GET USER

        return repo.findByUser(user);
    }
}