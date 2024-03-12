package edu.uoc.epcsd.notification.application.entities;

import edu.uoc.epcsd.notification.application.kafka.EmailMessage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class EmailMessageTest {
    @Test
    public void testEmailMessage() {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo("test@example.com");
        emailMessage.setSubject("Test Subject");
        emailMessage.setTemplateName("Test Template");
        emailMessage.setBody("Test Body");
        emailMessage.setButtonLink("Test Link");
        emailMessage.setButtonText("Test Button");

        assertEquals("test@example.com", emailMessage.getTo());
        assertEquals("Test Subject", emailMessage.getSubject());
        assertEquals("Test Template", emailMessage.getTemplateName());
        assertEquals("Test Body", emailMessage.getBody());
        assertEquals("Test Link", emailMessage.getButtonLink());
        assertEquals("Test Button", emailMessage.getButtonText());
    }

    @Test
    void testToString() {
        // Crea un objeto EmailMessage con valores específicos
        EmailMessage emailMessage = EmailMessage.builder()
                .to("test@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("Test Body")
                .buttonLink("https://example.com")
                .buttonText("Click Here")
                .build();

        // Verifica que el resultado del método toString sea el esperado
        String expectedToString = "EmailMessage(to=test@example.com, subject=Test Subject, templateName=template.html, body=Test Body, buttonLink=https://example.com, buttonText=Click Here)";
        assertThat(emailMessage.toString()).isEqualTo(expectedToString);
    }
}