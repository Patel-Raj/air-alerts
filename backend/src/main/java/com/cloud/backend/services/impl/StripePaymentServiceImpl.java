package com.cloud.backend.services.impl;

import com.cloud.backend.dto.CreateSubscriptionRequest;
import com.cloud.backend.dto.SubscriptionResponse;
import com.cloud.backend.entities.PaymentEntity;
import com.cloud.backend.enumerations.PaymentStatus;
import com.cloud.backend.repositories.PaymentRepository;
import com.cloud.backend.services.PaymentService;
import com.cloud.backend.services.SubscriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static com.cloud.backend.utils.Constants.*;


@Service
public class StripePaymentServiceImpl implements PaymentService {

    private Logger logger = Logger.getLogger(StripePaymentServiceImpl.class.getName());

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @Value("${stripe.webhook-key}")
    private String stripeWebhookKey;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    @Lazy
    private SubscriptionService subscriptionService;

    @Override
    public SubscriptionResponse createPaymentIntent(Long userId, CreateSubscriptionRequest request) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(PRICE * 100)
                        .setCurrency("cad")
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        Long subscriptionId = subscriptionService.createSubscription(userId, request);
        savePayment(subscriptionId, paymentIntent.getId());

        return SubscriptionResponse.builder()
                .clientSecret(paymentIntent.getClientSecret())
                .subscriptionId(subscriptionId)
                .build();
    }

    private void savePayment(Long subscriptionId, String paymentIntentId) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paymentIntentId(paymentIntentId)
                .subscriptionId(subscriptionId)
                .paymentStatus(PaymentStatus.CREATED)
                .amount(PRICE)
                .build();

        paymentRepository.save(paymentEntity);
    }

    @Override
    public void processWebhook(String payload, String stripeSignature) throws JsonProcessingException {

        if (stripeSignature == null) {
            return;
        }

        Event event;
        try {
            event = ApiResource.GSON.fromJson(payload, Event.class);
        } catch (JsonSyntaxException e) {
            return;
        }

        if(stripeWebhookKey != null && stripeSignature != null) {
            try {
                event = Webhook.constructEvent(payload, stripeSignature, stripeWebhookKey);
            } catch (SignatureVerificationException e) {
                return;
            }
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        }

        switch (event.getType()) {
            case STRIPE_INTENT_SUCCESS:
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                updatePaymentStatus(paymentIntent.getId(), PaymentStatus.SUCCEEDED);
                subscriptionService.enableSubscription(paymentIntent.getId());
                break;
            case STRIPE_INTENT_FAILED:
                logger.warning("Unhandled payment failed " + ((PaymentIntent) stripeObject).getId());
                updatePaymentStatus(((PaymentIntent) stripeObject).getId(), PaymentStatus.FAILED);
                break;
            default:
                logger.warning("Unhandled event " + event.getType());
                break;
        }
    }

    @Override
    public PaymentEntity findByPaymentIntentId(String paymentIntentId) {
        return paymentRepository.findByPaymentIntentId(paymentIntentId);
    }

    private void updatePaymentStatus(String paymentIntentId, PaymentStatus paymentStatus) {
        PaymentEntity paymentEntity = paymentRepository.findByPaymentIntentId(paymentIntentId);
        paymentEntity.setPaymentStatus(paymentStatus);
        paymentRepository.save(paymentEntity);
    }
}
