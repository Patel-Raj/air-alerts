package com.cloud.backend.services;

import com.cloud.backend.dto.CreateSubscriptionRequest;
import com.cloud.backend.dto.SubscriptionResponse;
import com.cloud.backend.entities.PaymentEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;

public interface PaymentService {

    SubscriptionResponse createPaymentIntent(Long userId, CreateSubscriptionRequest request) throws StripeException;

    void processWebhook(String payload, String stripeSignature) throws JsonProcessingException;

    PaymentEntity findByPaymentIntentId(String paymentIntentId);
}
