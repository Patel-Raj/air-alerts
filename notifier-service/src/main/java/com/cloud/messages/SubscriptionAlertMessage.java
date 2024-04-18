package com.cloud.messages;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SubscriptionAlertMessage {

    private String email;

    private String message;
}

