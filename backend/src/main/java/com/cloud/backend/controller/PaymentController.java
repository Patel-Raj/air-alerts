package com.cloud.backend.controller;

import com.cloud.backend.dto.CreateSubscriptionRequest;
import com.cloud.backend.dto.SubscriptionResponse;
import com.cloud.backend.services.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.cloud.backend.utils.Constants.SUCCESS;

@RequiredArgsConstructor
@RestController
@CrossOrigin("${cross.origin.allowed-origins}")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public SubscriptionResponse paymentIntent(@RequestBody CreateSubscriptionRequest request) throws StripeException {
        return paymentService.createPaymentIntent(request.getUserId(), request);
    }

    @PostMapping("/rest/auth/stripe/webhook")
    public String webhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String stripeSignature) throws JsonProcessingException {
        paymentService.processWebhook(payload, stripeSignature);
        return SUCCESS;
    }
}
