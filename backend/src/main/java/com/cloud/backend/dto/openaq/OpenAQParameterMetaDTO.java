package com.cloud.backend.dto.openaq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAQParameterMetaDTO {

    private Long id;

    private String displayName;

    private String description;

    private String preferredUnit;
}
