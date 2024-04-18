package com.cloud.services;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.cloud.messages.SubscriptionAlertMessage;
import com.cloud.utils.HtmlParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.mail.MessagingException;
import java.io.IOException;

public class SubscriptionStrategy implements EmailStrategy {
    private static SubscriptionStrategy subscriptionStrategy;

    private SubscriptionStrategy() {

    }

    public static synchronized SubscriptionStrategy getInstance() {
        if (subscriptionStrategy == null) {
            subscriptionStrategy = new SubscriptionStrategy();
        }
        return subscriptionStrategy;
    }

    @Override
    public void sendEmail(String payload, LambdaLogger logger) {
        ObjectMapper objectMapper = new ObjectMapper();
        SubscriptionAlertMessage subscriptionAlertMessage = new SubscriptionAlertMessage();
        try {
            subscriptionAlertMessage = objectMapper.readValue(payload.toString(), SubscriptionAlertMessage.class);
            EmailService emailService = EmailService.getInstance();
            String content = HtmlParser.parseHtml("subscription_email_template.html");
            content = content.replace("{{content}}", subscriptionAlertMessage.getMessage());
            emailService.sendEmail(subscriptionAlertMessage.getEmail(), "Subscription Alert", content);
        } catch (JsonProcessingException e) {
            logger.log("Error parsing: " + e);
        } catch (MessagingException e) {
            logger.log("Error sending email: " + e);
        } catch (IOException e) {
            logger.log("Error parsing html: " + e);
        }
        logger.log(subscriptionAlertMessage.toString());
    }
}