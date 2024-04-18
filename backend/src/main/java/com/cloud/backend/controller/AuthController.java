package com.cloud.backend.controller;

import com.cloud.backend.dto.LoginRequest;
import com.cloud.backend.dto.RegistrationDTO;
import com.cloud.backend.dto.VerifyEmail;
import com.cloud.backend.exceptions.UserExistsException;
import com.cloud.backend.exceptions.UserNotExistsException;
import com.cloud.backend.dto.LoginResponse;
import com.cloud.backend.dto.ResponseMessage;
import com.cloud.backend.services.AuthService;
import com.cloud.backend.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/auth")
@CrossOrigin("${cross.origin.allowed-origins}")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.login(loginRequest);
            LoginResponse responseMessage = new LoginResponse(true, Constants.SUCCESS, token);
            return ResponseEntity.ok(responseMessage);
        } catch (UserNotExistsException e) {
            LoginResponse responseMessage = new LoginResponse(false, e.getMessage(), "");
            return ResponseEntity.ok(responseMessage);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@RequestBody RegistrationDTO registrationDTO) {
        try {
            authService.registerUser(registrationDTO);
            ResponseMessage responseMessage = new ResponseMessage(true, Constants.SUCCESS );
            return ResponseEntity.ok(responseMessage);
        } catch (UserExistsException e) {
            ResponseMessage responseMessage = new ResponseMessage(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMessage);
        } catch (JsonProcessingException e) {
            ResponseMessage responseMessage = new ResponseMessage(false, "Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ResponseMessage> verifyEmail(@RequestBody VerifyEmail verifyEmail) {
        try {
            authService.verifyEmail(verifyEmail);
            ResponseMessage responseMessage = new ResponseMessage(true, Constants.SUCCESS);
            return ResponseEntity.ok(responseMessage);
        } catch (UserNotExistsException e) {
            ResponseMessage responseMessage = new ResponseMessage(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> statusCheck() {
        return ResponseEntity.ok("Success");
    }
}
