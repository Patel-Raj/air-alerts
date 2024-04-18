package com.cloud.services;


import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.cloud.messages.VerifyEmailMessage;
import com.cloud.utils.HtmlParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.mail.MessagingException;
import java.io.IOException;

public class VerifyEmailStrategy implements EmailStrategy {


    private static VerifyEmailStrategy verifyEmailStrategy;

    private VerifyEmailStrategy() {

    }

    public static synchronized VerifyEmailStrategy getInstance() {
        if (verifyEmailStrategy == null) {
            verifyEmailStrategy = new VerifyEmailStrategy();
        }
        return verifyEmailStrategy;
    }

    @Override
    public void sendEmail(String payload, LambdaLogger logger) {
        ObjectMapper objectMapper = new ObjectMapper();
        VerifyEmailMessage verifyEmailMessage = new VerifyEmailMessage();
        try {
            verifyEmailMessage = objectMapper.readValue(payload.toString(), VerifyEmailMessage.class);
            EmailService emailService = EmailService.getInstance();
            String content = HtmlParser.parseHtml("verify_email_template.html");
            content = content.replace("{{verification_link}}", verifyEmailMessage.getUrl());
            emailService.sendEmail(verifyEmailMessage.getEmail(), "Verify your email", content);
        } catch (JsonProcessingException e) {
            logger.log("Error parsing: " + e);
        } catch (MessagingException e) {
            logger.log("Error sending email: " + e);
        } catch (IOException e) {
            logger.log("Error parsing html: " + e);
        }
        logger.log(verifyEmailMessage.toString());
    }
}
