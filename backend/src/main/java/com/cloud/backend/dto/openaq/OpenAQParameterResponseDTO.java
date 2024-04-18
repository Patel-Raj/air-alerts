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
public class OpenAQParameterResponseDTO {

    private List<OpenAQLocationResultsDTO> results;

}
