package com.cloud.backend.dto.openaq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAQParameterDTO {

    private Long parameterId;

    private Double lastValue;
}
