package com.cloud.services;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

public interface EmailStrategy {
    void sendEmail(String payload, LambdaLogger logger);
}
