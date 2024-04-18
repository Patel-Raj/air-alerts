package com.cloud.backend.services.impl;


import com.cloud.backend.dto.VerifyEmailMessage;
import com.cloud.backend.services.QueueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;

import static com.cloud.backend.utils.Constants.MESSAGE_GROUP_ID;
import static com.cloud.backend.utils.Constants.VERIFY_EMAIL_MESSAGE;

@Service
public class AwsQueueServiceImpl implements QueueService {

    private static final Logger logger = LoggerFactory.getLogger(AwsQueueServiceImpl.class);

    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void sendVerifyEmailMessage(String email, String url) throws JsonProcessingException {
        VerifyEmailMessage message = VerifyEmailMessage
                .builder()
                .email(email)
                .url(url)
                .build();

        String jsonMessage = objectMapper.writeValueAsString(message);
        MessageAttributeValue messageAttribute = MessageAttributeValue.builder()
                .dataType("String")
                .stringValue(VERIFY_EMAIL_MESSAGE)
                .build();

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put(VERIFY_EMAIL_MESSAGE, messageAttribute);

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(jsonMessage)
                .messageGroupId(MESSAGE_GROUP_ID)
                .messageDeduplicationId(url)
                .messageAttributes(messageAttributes)
                .build();

        sqsClient.sendMessage(request);
    }
}
