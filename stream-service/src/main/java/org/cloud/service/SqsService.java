package org.cloud.service;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloud.model.SubscriptionAlertMessage;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.cloud.util.Constants.*;

public class SqsService {

    private LambdaLogger logger;

    private SqsClient sqsClient;

    private String outgoingQueueUrl;

    private ObjectMapper objectMapper;


    public SqsService(LambdaLogger logger) {
        this.logger = logger;
        this.sqsClient = SqsClient.builder().region(Region.US_EAST_1).build();
        this.outgoingQueueUrl = System.getenv().get(OUTGOING_QUEUE_URL);
        this.objectMapper = new ObjectMapper();
    }

    public void processOutgoingResults(SubscriptionAlertMessage subscriptionAlertMessage) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(subscriptionAlertMessage);
        MessageAttributeValue messageAttribute = MessageAttributeValue.builder()
                .dataType("String")
                .stringValue(SUBSCRIPTION_ALERT)
                .build();

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put(SUBSCRIPTION_ALERT, messageAttribute);

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(outgoingQueueUrl)
                .messageBody(jsonMessage)
                .messageGroupId(subscriptionAlertMessage.getEmail())
                .messageDeduplicationId(UUID.randomUUID().toString())
                .messageAttributes(messageAttributes)
                .build();

        sqsClient.sendMessage(request);
    }
}
