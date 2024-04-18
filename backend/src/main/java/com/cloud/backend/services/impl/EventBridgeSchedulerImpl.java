package com.cloud.backend.services.impl;


import com.cloud.backend.dto.eventbridge.EventPayload;
import com.cloud.backend.entities.SubscriptionEntity;
import com.cloud.backend.entities.UserEntity;
import com.cloud.backend.exceptions.UserNotExistsException;
import com.cloud.backend.repositories.UserRepository;
import com.cloud.backend.services.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.*;

import java.util.Optional;
import java.util.UUID;

import static com.cloud.backend.utils.Constants.ALERT_TEMPLATE;

@Service
public class EventBridgeSchedulerImpl implements EventService {

    @Autowired
    private UserRepository userRepository;

    @Value("${aws.stream-service.arn}")
    private String streamServiceARN;

    @Value("${aws.lab-role.arn}")
    private String labRoleArn;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String createSchedule(Long userId, SubscriptionEntity subscription) throws JsonProcessingException {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new UserNotExistsException("User does not exists");
        }

        EventPayload eventPayload = EventPayload.builder()
                .email(userEntityOptional.get().getEmail())
                .template(getFormattedTemplate(subscription))
                .parameterId(subscription.getParameterId())
                .locationId(subscription.getLocationId())
                .build();

        return createEventBridgeSchedule(eventPayload, subscription.getCronSchedule());

    }

    @Override
    public void removeSchedule(String scheduleId) {
        SchedulerClient schedulerClient = SchedulerClient.builder()
                .region(Region.US_EAST_1)
                .build();


        DeleteScheduleRequest request = DeleteScheduleRequest.builder()
                .name(scheduleId)
                .build();

        schedulerClient.deleteSchedule(request);
    }

    private String getFormattedTemplate(SubscriptionEntity subscription) {
        return ALERT_TEMPLATE
                .replace("<PARAMETER>", subscription.getDisplayName())
                .replace("<CITY>", subscription.getCity())
                .replace("<COUNTRY>", subscription.getCountryCode())
                .replace("<UNIT>", subscription.getUnit());
    }

    private String createEventBridgeSchedule(EventPayload eventPayload, String cron) throws JsonProcessingException {
        String scheduleId = UUID.randomUUID().toString();

        SchedulerClient schedulerClient = SchedulerClient.builder()
                .region(Region.US_EAST_1)
                .build();

        CreateScheduleRequest createScheduleRequest = CreateScheduleRequest.builder()
                .name(scheduleId)
                .scheduleExpression(cron)
                .target(software.amazon.awssdk.services.scheduler.model.Target.builder()
                        .roleArn(labRoleArn)
                        .arn(streamServiceARN)
                        .input(objectMapper.writeValueAsString(eventPayload))
                        .build())
                .flexibleTimeWindow(FlexibleTimeWindow.builder()
                        .mode(FlexibleTimeWindowMode.OFF)
                        .build())
                .actionAfterCompletion(ActionAfterCompletion.DELETE)
                .build();

        schedulerClient.createSchedule(createScheduleRequest);

        return scheduleId;
    }
}
