package com.ecommerce.payment;

import com.ecommerce.entity.Order;
import com.ecommerce.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    private final OrderRepository orderRepo;

    public String createPaymentSession(Order order) throws Exception {

        Stripe.apiKey = secretKey;

        Session session = Session.create(
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:5173/success")
                        .setCancelUrl("http://localhost:5173/cancel")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("inr")
                                                        .setUnitAmount((long)(order.getTotalPrice() * 100))
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("Order Payment")
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .build()
        );

        // 🔥 CRITICAL FIX
        order.setStripeSessionId(session.getId());
        orderRepo.save(order); // 👈 THIS WAS MISSING

        return session.getUrl();
    }
}