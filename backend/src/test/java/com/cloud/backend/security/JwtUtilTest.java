package com.cloud.backend.security;

import com.cloud.backend.entities.UserEntity;
import com.cloud.backend.enumerations.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static com.cloud.backend.utils.Constants.AUTH_HEADER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(jwtUtil, "accessTokenValidity", 86400000L);
        ReflectionTestUtils.setField(jwtUtil, "secretKey", "askjdfhsadkjfbhsadkjfbsadkhfbasdjkdbfaskjd");
    }

    @Test
    void createToken() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("test");
        userEntity.setRole(Role.USER);

        // Act
        String token = jwtUtil.createToken(userEntity);

        // Assert
        Assertions.assertNotNull(token);
    }

    @Test
    void test_ResolveClaims_Success() {
        // Arrange
        Claims expectedClaims = new DefaultClaims();
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        when(httpRequest.getHeader(AUTH_HEADER)).thenReturn("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        when(jwtUtil.resolveToken(httpRequest)).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

        // Assert
        Claims resolvedClaims = jwtUtil.resolveClaims(httpRequest);

        // Act
        assertNull(resolvedClaims);
    }

    @Test
    void resolveToken() {
        // Arrange
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        when(httpRequest.getHeader(AUTH_HEADER)).thenReturn("Bearer 8r78937892378937948");

        // Act
        String token = jwtUtil.resolveToken(httpRequest);

        // Assert
        assertEquals("8r78937892378937948", token);
    }

    @Test
    void test_ValidateClaims_Valid() {
        // Arrange
        JwtUtil jwtUtil = new JwtUtil();
        Claims claims = mock(Claims.class);
        Date futureDate = new Date(System.currentTimeMillis() + 86400000L);

        when(claims.getExpiration()).thenReturn(futureDate);

        // Act
        boolean isValid = jwtUtil.validateClaims(claims);

        // Assert
        assertTrue(isValid);
    }
}