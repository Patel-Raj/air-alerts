package com.cloud.backend.services.impl;

import com.cloud.backend.dto.LoginRequest;
import com.cloud.backend.dto.RegistrationDTO;
import com.cloud.backend.dto.VerifyEmail;
import com.cloud.backend.entities.UserEntity;
import com.cloud.backend.entities.VerifyEmailEntity;
import com.cloud.backend.enumerations.Role;
import com.cloud.backend.enumerations.UserStatus;
import com.cloud.backend.exceptions.UserExistsException;
import com.cloud.backend.exceptions.UserNotExistsException;
import com.cloud.backend.repositories.UserRepository;
import com.cloud.backend.repositories.VerifyEmailRepository;
import com.cloud.backend.security.JwtUtil;
import com.cloud.backend.services.AuthService;
import com.cloud.backend.services.QueueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private VerifyEmailRepository verifyEmailRepository;

    @Autowired
    private QueueService queueService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void registerUser(RegistrationDTO registrationDTO) throws JsonProcessingException {
        checkIfUserExists(registrationDTO);

        UserEntity user = saveUser(registrationDTO);

        String verificationLink = generateEmailVerificationLink(user);

        queueService.sendVerifyEmailMessage(user.getEmail(), verificationLink);
    }

    private void checkIfUserExists(RegistrationDTO registrationDTO) {
        Optional<UserEntity> emailOpt = userRepository.findByEmail(registrationDTO.getEmail());
        if (emailOpt.isPresent()) {
            throw new UserExistsException("User already exists.");
        }
    }

    private UserEntity saveUser(RegistrationDTO registrationDTO) {
        UserEntity user = UserEntity.builder()
                .email(registrationDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(registrationDTO.getPassword()))
                .role(Role.USER)
                .name(registrationDTO.getName())
                .userStatus(UserStatus.UNVERIFIED)
                .build();
        return userRepository.save(user);
    }

    private String generateEmailVerificationLink(UserEntity user) {
        String uniqueId = java.util.UUID.randomUUID().toString();
        VerifyEmailEntity verifyEmailEntity = VerifyEmailEntity.builder()
                .userId(user.getId())
                .uniqueId(uniqueId)
                .build();
        verifyEmailRepository.save(verifyEmailEntity);
        return frontendUrl + "verify-email/" + uniqueId;
    }

    private String getToken(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String authEmail = authentication.getName();

            UserEntity user = userRepository.findByEmail(authEmail).get();

            return jwtUtil.createToken(user);
        } catch (Exception e) {
            throw new UserNotExistsException("Invalid Password");
        }
    }

    @Override
    public String login(LoginRequest loginRequest) {
        Optional<UserEntity> responseOpt = userRepository.findByEmail(loginRequest.getEmail());
        if (responseOpt.isEmpty()) {
            throw new UserNotExistsException("User does not exists");
        }
        if (responseOpt.get().getUserStatus().equals(UserStatus.UNVERIFIED)) {
            throw new UserNotExistsException("Email not verified");
        }

        return getToken(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @Override
    public void verifyEmail(VerifyEmail verifyEmail) {
        Optional<VerifyEmailEntity> responseOpt = verifyEmailRepository.findByUniqueId(verifyEmail.getId());
        if (responseOpt.isEmpty()) {
            throw new UserNotExistsException("User does not exists.");
        }

        UserEntity user = userRepository.findById(responseOpt.get().getUserId()).get();
        user.setUserStatus(UserStatus.VERIFIED);
        userRepository.save(user);
    }
}
