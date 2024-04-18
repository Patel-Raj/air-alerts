package com.cloud;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.cloud.enumerations.MessageType;
import com.cloud.services.EmailStrategy;
import com.cloud.services.SendEmailFactory;

import java.util.Map;

public class Handler implements RequestHandler<SQSEvent, String> {

    private SendEmailFactory sendEmailFactory = SendEmailFactory.getInstance();

    @Override
    public String handleRequest(SQSEvent sqsEvent, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Event: " + sqsEvent.toString());

        for (SQSEvent.SQSMessage message : sqsEvent.getRecords()) {
            MessageType messageType = getMessageType(message);
            EmailStrategy emailStrategy = sendEmailFactory.getEmailStrategy(messageType);
            emailStrategy.sendEmail(message.getBody(), logger);
        }
        return "Execution Complete";
    }

    private MessageType getMessageType(SQSEvent.SQSMessage message) {
        Map<String, SQSEvent.MessageAttribute> messageAttributes = message.getMessageAttributes();
        for(Map.Entry<String, SQSEvent.MessageAttribute> entry : messageAttributes.entrySet()) {
            return MessageType.valueOf(entry.getKey());
        }
        return null;
    }
}