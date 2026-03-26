package com.ecommerce.controller;

import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.entity.Order;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentWebhookController {

    private final OrderRepository orderRepo;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/webhook")
    public String handleWebhook(@RequestBody String payload,
                                @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            System.out.println("🔥 WEBHOOK HIT");

            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            System.out.println("EVENT TYPE: " + event.getType());

            if ("checkout.session.completed".equals(event.getType())) {

                // 🔥 FIX: manual deserialization
                String json = event.getData().getObject().toJson();

                Session session = Session.GSON.fromJson(json, Session.class);

                String sessionId = session.getId();
                System.out.println("SESSION FROM STRIPE: " + sessionId);

                Order order = orderRepo.findByStripeSessionId(sessionId);

                System.out.println("DB LOOKUP RESULT: " + order);

                if (order != null) {
                    order.setStatus("PAID");
                    orderRepo.save(order);
                    System.out.println("✅ ORDER UPDATED TO PAID");
                } else {
                    System.out.println("❌ ORDER NOT FOUND");
                }
            }

            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
