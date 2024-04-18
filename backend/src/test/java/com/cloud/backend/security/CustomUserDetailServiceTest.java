package com.cloud.backend.security;

import com.cloud.backend.entities.UserEntity;
import com.cloud.backend.enumerations.Role;
import com.cloud.backend.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @Test
    void testLoadUser_Success() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("test");
        userEntity.setRole(Role.USER);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails userDetails = customUserDetailService.loadUserByUsername("test@example.com");

        // Assert
        verify(userRepository, times(1)).findByEmail("test@example.com");
        Assertions.assertEquals("test@example.com", userDetails.getUsername());
    }


    @Test
    void testLoadUser_Failure() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act
        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> customUserDetailService.loadUserByUsername("test@example.com")
        );

        // Assert
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }


}