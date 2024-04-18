package org.cloud;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.cloud.model.SubscriptionAlertMessage;
import org.cloud.openaq.Api;
import org.cloud.service.SqsService;

import java.io.IOException;
import java.util.Map;

public class HandleEventBridge implements RequestHandler<Map<String, String>, String> {

    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Custom Log: Event Details " + event.toString());

        try {
            String response = Api.getResponse(event, logger);
            sendToOutgoingQueue(response, event.get("email"), logger);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Stream Event Handled";
    }



    private void sendToOutgoingQueue(String response, String email, LambdaLogger logger) throws JsonProcessingException {
        SubscriptionAlertMessage subscriptionAlertMessage = SubscriptionAlertMessage
                .builder()
                .email(email)
                .message(response).build();

        SqsService sqsService = new SqsService(logger);
        sqsService.processOutgoingResults(subscriptionAlertMessage);
    }
}