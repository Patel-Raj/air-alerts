package com.cloud.backend.dto.openaq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAQLocationResultsDTO {

    private Long id;

    private String city;

    private String country;

    private List<OpenAQParameterDTO> parameters;
}
