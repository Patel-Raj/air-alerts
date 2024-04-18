package com.cloud.backend.services.impl;

import com.cloud.backend.dto.CreateSubscriptionRequest;
import com.cloud.backend.dto.UnsubscribeRequest;
import com.cloud.backend.entities.SubscriptionEntity;
import com.cloud.backend.enumerations.SubscriptionStatus;
import com.cloud.backend.exceptions.SubscriptionException;
import com.cloud.backend.repositories.SubscriptionRepository;
import com.cloud.backend.services.EventService;
import com.cloud.backend.services.PaymentService;
import com.cloud.backend.services.SubscriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private PaymentService paymentService;

    @Override
    public Optional<SubscriptionEntity> getUserSubscriptionByLocationIdAndParameterIdAndSubscriptionStatus(Long userId, Long locationId, Long parameterId, SubscriptionStatus subscriptionStatus) {
        return subscriptionRepository.findByUserIdAndLocationIdAndParameterIdAndSubscriptionStatus(userId, locationId, parameterId, subscriptionStatus);
    }

    @Override
    public Long createSubscription(Long userId, CreateSubscriptionRequest request) {
        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .cronSchedule(request.getCronSchedule())
                .city(request.getCity())
                .countryCode(request.getCountryCode())
                .userId(userId)
                .unit(request.getUnit())
                .description(request.getDescription())
                .locationId(request.getLocationId())
                .parameterId(request.getParameterId())
                .displayName(request.getDisplayName())
                .subscriptionStatus(SubscriptionStatus.PENDING)
                .build();

        SubscriptionEntity savedSubscription = subscriptionRepository.save(subscription);
        return savedSubscription.getId();
    }

    @Override
    public void unsubscribe(UnsubscribeRequest request) {
        Optional<SubscriptionEntity> subscriptionEntityOptional = subscriptionRepository.findById(request.getSubscriptionId());
        if (subscriptionEntityOptional.isEmpty()) {
            throw new SubscriptionException("Subscription not found");
        }

        SubscriptionEntity subscription = subscriptionEntityOptional.get();
        String scheduleId =subscription.getEventBridgeScheduleId();
        eventService.removeSchedule(scheduleId);
        subscription.setSubscriptionStatus(SubscriptionStatus.UNSUBSCRIBED);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void enableSubscription(String paymentIntentId) throws JsonProcessingException {
        Long subscriptionId = paymentService.findByPaymentIntentId(paymentIntentId).getSubscriptionId();
        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId).get();
        String scheduleId = eventService.createSchedule(subscription.getUserId(), subscription);

        subscription.setEventBridgeScheduleId(scheduleId);
        subscription.setSubscriptionStatus(SubscriptionStatus.SUBSCRIBED);
        subscriptionRepository.save(subscription);
    }
}
