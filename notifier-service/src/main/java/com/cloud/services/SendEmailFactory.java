package com.cloud.services;

import com.cloud.enumerations.MessageType;

public class SendEmailFactory {

    private static SendEmailFactory sendEmailFactory;

    private SendEmailFactory() {

    }

    public static synchronized SendEmailFactory getInstance() {
        if (sendEmailFactory == null) {
            sendEmailFactory = new SendEmailFactory();
        }
        return sendEmailFactory;
    }

    public EmailStrategy getEmailStrategy(MessageType messageType) {
        switch (messageType) {
            case VERIFY_EMAIL_MESSAGE:
                return VerifyEmailStrategy.getInstance();

            case SUBSCRIPTION_ALERT:
                return SubscriptionStrategy.getInstance();
        }
        return null;
    }

}
