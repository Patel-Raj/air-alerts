package com.cloud.backend.services;

import com.cloud.backend.dto.CreateSubscriptionRequest;
import com.cloud.backend.entities.SubscriptionEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface EventService {
    String createSchedule(Long userId, SubscriptionEntity subscription) throws JsonProcessingException;

    void removeSchedule(String scheduleId);
}
