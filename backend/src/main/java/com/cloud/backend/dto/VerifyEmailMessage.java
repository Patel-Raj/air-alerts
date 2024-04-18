package com.cloud.backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class VerifyEmailMessage {

    private String email;

    private String url;

}
