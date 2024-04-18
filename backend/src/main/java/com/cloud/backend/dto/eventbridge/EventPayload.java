package com.cloud.backend.dto.eventbridge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventPayload {

    private String email;

    private String template;

    private Long locationId;

    private Long parameterId;
}
