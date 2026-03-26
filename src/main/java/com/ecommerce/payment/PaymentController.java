package com.ecommerce.payment;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.entity.Order;
import com.ecommerce.service.OrderService;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;


    @PostMapping("/{orderId}")
    public String pay(@PathVariable Long orderId) throws Exception {

        Order order = orderService.getOrderById(orderId);

        String url = paymentService.createPaymentSession(order);

        return url;
    }
}
