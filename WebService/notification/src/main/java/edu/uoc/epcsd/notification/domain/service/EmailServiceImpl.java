package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.application.kafka.EmailMessage;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    public static final String SMTP_HOST= "smtp.gmail.com";
    public static final String SMTP_PORT = "465";

    @Value(value = "${spring.mail.username}")
    public String smtpUsername;

    @Value(value ="${spring.mail.password}")
    public String smtpPassword;

    public void sendEmail(EmailMessage email) {
        Properties properties = configureEmailProperties();

        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        session.setDebug(false);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUsername));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
            message.setSubject(email.getSubject());
            message.setContent(getTemplateEmail(email), "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            //error
        }
    }
    public Properties configureEmailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        return properties;
    }

    public String getTemplateEmail(EmailMessage email) {
        String templateName = email.getTemplateName();
        StringBuilder content = new StringBuilder();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(templateName)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append(System.lineSeparator());
                }
            }
        } catch (NullPointerException | IOException e) {
            return email.getBody();
        }

        String templateContent = content.toString();
        if (templateContent.contains("${body}") && templateContent.contains("${buttonLink}") && templateContent.contains("${buttonText}")) {
            templateContent = templateContent.replace("${body}", email.getBody())
                    .replace("${buttonLink}", email.getButtonLink())
                    .replace("${buttonText}", email.getButtonText());
        }

        return templateContent;
    }



}
