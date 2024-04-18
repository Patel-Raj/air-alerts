package com.cloud.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface QueueService {

    void sendVerifyEmailMessage(String email, String url) throws JsonProcessingException;
}
