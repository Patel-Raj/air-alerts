package com.cloud.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class CreateSubscriptionRequest {

    private Long userId;

    private Long locationId;

    private Long parameterId;

    private String cronSchedule;

    private String city;

    private String countryCode;

    private String unit;

    private String description;

    private String displayName;
}
