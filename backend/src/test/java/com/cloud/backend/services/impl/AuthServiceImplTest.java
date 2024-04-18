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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerifyEmailRepository verifyEmailRepository;

    @Mock
    private PasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AwsQueueServiceImpl awsQueueServiceImpl;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Mock
    private SqsClient sqsClient;

    @Mock
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(authServiceImpl, "frontendUrl", "http://localhost:3000");
    }

    @Test
    void testRegisterUser_Exists_ThrowException() {
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .email("exists.user@gmail.com")
                .build();

        when(userRepository.findByEmail("exists.user@gmail.com")).thenReturn(Optional.of(new UserEntity()));

        Assertions.assertThrows(UserExistsException.class, () -> authServiceImpl.registerUser(registrationDTO));

        verify(userRepository, times(1)).findByEmail("exists.user@gmail.com");
    }

    @Test
    void testRegisterUser_Success() throws JsonProcessingException {
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .email("exists.user@gmail.com")
                .password("testpassword")
                .name("Test")
                .build();

        UserEntity user = UserEntity.builder()
                .email(registrationDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(registrationDTO.getPassword()))
                .role(Role.USER)
                .name(registrationDTO.getName())
                .userStatus(UserStatus.UNVERIFIED)
                .build();

        VerifyEmailEntity verifyEmailEntity = VerifyEmailEntity.builder()
                .userId(user.getId())
                .uniqueId("uuid-123-123")
                .build();

        when(userRepository.findByEmail("exists.user@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(verifyEmailRepository.save(any())).thenReturn(verifyEmailEntity);

        authServiceImpl.registerUser(registrationDTO);

        verify(awsQueueServiceImpl, times(1)).sendVerifyEmailMessage(any(), any());
    }

    @Test
    void test_login_UserNotExistsException() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@email.com")
                .password("abcd")
                .build();

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotExistsException.class, () -> authServiceImpl.login(loginRequest));
        verify(userRepository, times(1)).findByEmail("test@email.com");
    }

    @Test
    void test_login_Unverified() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@email.com")
                .password("abcd")
                .build();

        UserEntity response = UserEntity.builder()
                        .userStatus(UserStatus.UNVERIFIED).build();

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(response));

        Assertions.assertThrows(UserNotExistsException.class, () -> authServiceImpl.login(loginRequest));
        verify(userRepository, times(1)).findByEmail("test@email.com");
    }

    @Test
    void test_VerifyEmail_UserNotExistsException() {
        VerifyEmail verifyEmail = VerifyEmail.builder()
                .id("uuid-123-123")
                .build();

        VerifyEmailEntity verifyEmailEntity = VerifyEmailEntity.builder()
                .id(1L)
                .uniqueId("uuid-123-123")
                .build();


        when(verifyEmailRepository.findByUniqueId("uuid-123-123")).thenReturn(Optional.empty());


        Assertions.assertThrows(UserNotExistsException.class, () -> authServiceImpl.verifyEmail(verifyEmail));
    }

    @Test
    void test_VerifyEmail_Exists() {
        VerifyEmail verifyEmail = VerifyEmail.builder()
                .id("uuid-123-123")
                .build();

        VerifyEmailEntity verifyEmailEntity = VerifyEmailEntity.builder()
                .id(1L)
                .userId(1L)
                .uniqueId("uuid-123-123")
                .build();

        UserEntity user = UserEntity.builder()
                .userStatus(UserStatus.UNVERIFIED).build();

        when(verifyEmailRepository.findByUniqueId("uuid-123-123")).thenReturn(Optional.of(verifyEmailEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        authServiceImpl.verifyEmail(verifyEmail);

        verify(userRepository, times(1)).save(any());
    }
}