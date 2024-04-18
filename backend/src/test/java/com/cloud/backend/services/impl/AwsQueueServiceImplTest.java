package com.cloud.backend.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AwsQueueServiceImplTest {

    @InjectMocks
    private AwsQueueServiceImpl awsQueueService;

    @Mock
    private SqsClient sqsClient;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(awsQueueService, "queueUrl", "https://sqs.us-east-1.amazonaws.com/test/notifier-queue.fifo");
    }

    @Test
    void test_sendVerifyEmailMessage() throws JsonProcessingException {
        // Arrange
        String email = "test@example.com";
        String url = "https://example.com/verify";
        String messageJson = "{\"email\":\"test@example.com\",\"url\":\"https://example.com/verify\"}";

        when(objectMapper.writeValueAsString(any())).thenReturn(messageJson);

        // Act
        awsQueueService.sendVerifyEmailMessage(email, url);

        // Assert
        verify(objectMapper).writeValueAsString(any());
        verify(sqsClient).sendMessage(any(SendMessageRequest.class));
    }
}