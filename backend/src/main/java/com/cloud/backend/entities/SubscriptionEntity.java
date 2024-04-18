package com.cloud.backend.entities;

import com.cloud.backend.enumerations.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscription")
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Long locationId;

    private Long parameterId;

    private String cronSchedule;

    private String eventBridgeScheduleId;

    private String city;

    private String countryCode;

    private String unit;

    private String description;

    private String displayName;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;
}
