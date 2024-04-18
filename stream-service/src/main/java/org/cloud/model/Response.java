package org.cloud.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Response {

    private Long locationId;

    private List<Parameter> parameters;
}
