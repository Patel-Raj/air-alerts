package com.cloud.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailService customUserDetailService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Authentication authentication;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void test_doFilterInternal_Null() throws ServletException, IOException {
        // Arrange
        when(jwtUtil.resolveToken(request)).thenReturn(null);

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_AuthenticationSuccessful() throws ServletException, IOException {
        // Arrange
        String accessToken = "validToken";
        String email = "test@example.com";

        when(jwtUtil.resolveToken(request)).thenReturn(accessToken);

        Claims claims = Jwts.claims().setSubject(email);
        when(jwtUtil.resolveClaims(request)).thenReturn(claims);
        when(jwtUtil.validateClaims(claims)).thenReturn(true);

        when(customUserDetailService.loadUserByUsername(email)).thenReturn(userDetails);

        when(userDetails.getAuthorities()).thenReturn(null);

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_AuthenticationError() throws ServletException, IOException {
        // Arrange
        String accessToken = "validToken";
        String errorMessage = "Invalid token";

        when(jwtUtil.resolveToken(request)).thenReturn(accessToken);

        Claims claims = Jwts.claims().setSubject("test@example.com");
        when(jwtUtil.resolveClaims(request)).thenReturn(claims);
        when(jwtUtil.validateClaims(claims)).thenThrow(new RuntimeException(errorMessage));

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response, times(1)).setStatus(HttpStatus.FORBIDDEN.value());
        verify(response, times(1)).setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}