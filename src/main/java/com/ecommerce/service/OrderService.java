package com.ecommerce.service;

import com.ecommerce.config.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.ecommerce.entity.*;
import com.ecommerce.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemRepository cartRepo;
    private final OrderRepository orderRepo;

    private User getCurrentUser() {
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return userDetails.getUser();
    }

    public Order getOrderById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order placeOrder(String address) {

        User user = getCurrentUser();

        List<CartItem> cartItems = cartRepo.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<OrderItem> orderItems = cartItems.stream().map(item -> {
            OrderItem oi = new OrderItem();
            oi.setProductName(item.getProduct().getName());
            oi.setQuantity(item.getQuantity());
            oi.setPrice(item.getProduct().getPrice());
            return oi;
        }).collect(Collectors.toList());

        double total = cartItems.stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setItems(orderItems);
        order.setTotalPrice(total);
        order.setStatus("PENDING");
        order.setAddress(address);

        // clear cart
        cartRepo.deleteAll(cartItems);

        return orderRepo.save(order);
    }

    public List<Order> getUserOrders() {
        User user = getCurrentUser();
        return orderRepo.findByUser(user);
    }
}