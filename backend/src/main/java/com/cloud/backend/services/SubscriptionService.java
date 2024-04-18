package com.cloud.backend.services;


import com.cloud.backend.dto.CreateSubscriptionRequest;
import com.cloud.backend.dto.UnsubscribeRequest;
import com.cloud.backend.entities.SubscriptionEntity;
import com.cloud.backend.enumerations.SubscriptionStatus;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public interface SubscriptionService {

    Optional<SubscriptionEntity> getUserSubscriptionByLocationIdAndParameterIdAndSubscriptionStatus(Long userId, Long locationId, Long parameterId, SubscriptionStatus status);

    Long createSubscription(Long userId, CreateSubscriptionRequest request);

    void unsubscribe(UnsubscribeRequest request);

    void enableSubscription(String paymentIntentId) throws JsonProcessingException;
}
