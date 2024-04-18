package com.cloud.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ParameterResponse {

    private Long locationId;

    private Boolean subscribed;

    private String city;

    private String countryCode;

    private Long parameterId;

    private String unit;

    private String description;

    private Long subscriptionId;

    private String cronSchedule;

    private Double value;

    private String displayName;
}
