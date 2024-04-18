package com.cloud.backend.services.impl;

import com.cloud.backend.entities.SubscriptionEntity;
import com.cloud.backend.entities.UserEntity;
import com.cloud.backend.enumerations.SubscriptionStatus;
import com.cloud.backend.enumerations.UserStatus;
import com.cloud.backend.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.scheduler.model.SchedulerException;
import software.amazon.awssdk.services.scheduler.model.ValidationException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventBridgeSchedulerImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventBridgeSchedulerImpl eventBridgeScheduler;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(eventBridgeScheduler, "streamServiceARN", "streamServiceARN");
        ReflectionTestUtils.setField(eventBridgeScheduler, "labRoleArn", "labRoleArn");
    }

    @Test
    void createSchedule() throws JsonProcessingException {
        Long userId = 10L;

        UserEntity user = UserEntity.builder().userStatus(UserStatus.VERIFIED).name("test").id(userId).email("test@example.com").build();
        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .id(1L)
                .userId(10L)
                .locationId(456L)
                .parameterId(789L)
                .cronSchedule("0 0 * * *")
                .eventBridgeScheduleId("schedule123")
                .city("New York")
                .countryCode("US")
                .unit("metric")
                .description("Weather forecast subscription")
                .displayName("Weather Forecast")
                .subscriptionStatus(SubscriptionStatus.SUBSCRIBED)
                .build();

        String json = "{\"email\":\"test@example.com\",\"template\":\"example_template\",\"locationId\":12345,\"parameterId\":67890}";

        when(objectMapper.writeValueAsString(any())).thenReturn(json);
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        Assertions.assertThrows(ValidationException.class, () -> eventBridgeScheduler.createSchedule(userId, subscription));
    }

    @Test
    void removeSchedule() {
        Assertions.assertThrows(SchedulerException.class, () -> eventBridgeScheduler.removeSchedule("uuid-123-123"));
    }
}