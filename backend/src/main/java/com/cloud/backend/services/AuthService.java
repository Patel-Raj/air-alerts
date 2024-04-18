package com.cloud.backend.services;

import com.cloud.backend.dto.LoginRequest;
import com.cloud.backend.dto.RegistrationDTO;
import com.cloud.backend.dto.VerifyEmail;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuthService {

    void registerUser(RegistrationDTO registrationDTO) throws JsonProcessingException;

    String login(LoginRequest loginRequest);

    void verifyEmail(VerifyEmail verifyEmail);
}
