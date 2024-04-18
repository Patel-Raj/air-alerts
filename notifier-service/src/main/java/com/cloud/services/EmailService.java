package com.cloud.services;

import com.cloud.utils.Constants;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;


public class EmailService {

    private static EmailService emailService;

    private static Session session;

    private EmailService() {

    }

    public static synchronized EmailService getInstance() {
        if (emailService == null) {
            emailService = EmailService.createEmailService();
        }
        return emailService;
    }

    private static EmailService createEmailService() {
        Map<String, String> envVariables = System.getenv();
        Properties props = new Properties();
        props.put(Constants.EMAIL_AUTH, "true");
        props.put(Constants.EMAIL_START_TTLS, "true");
        props.put(Constants.EMAIL_HOST, "smtp.gmail.com");
        props.put(Constants.EMAIL_PORT, "587");

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(envVariables.get(Constants.EMAIL_USERNAME), envVariables.get(Constants.EMAIL_PASSWORD));
            }
        });

        return new EmailService();
    }

    public void sendEmail(String to, String subject, String content) throws MessagingException {
        Map<String, String> envVariables = System.getenv();
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(envVariables.get(Constants.EMAIL_USERNAME)));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(content, "text/html");

        Transport.send(message);
    }
}
