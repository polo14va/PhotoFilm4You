package edu.uoc.epcsd.user.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailMessageTest {

    @Test
    void createEmailMessageInstance() {
        EmailMessage emailMessage = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();

        assertThat(emailMessage).isNotNull();
        assertThat(emailMessage.getTo()).isEqualTo("recipient@example.com");
        assertThat(emailMessage.getSubject()).isEqualTo("Test Subject");
        assertThat(emailMessage.getTemplateName()).isEqualTo("template.html");
        assertThat(emailMessage.getBody()).isEqualTo("This is the email body.");
        assertThat(emailMessage.getButtonLink()).isEqualTo("https://example.com");
        assertThat(emailMessage.getButtonText()).isEqualTo("Click me");
    }

    @Test
    void testEqualsAndHashCode() {
        EmailMessage message1 = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();

        EmailMessage message2 = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();

        EmailMessage message3 = EmailMessage.builder()
                .to("other@example.com")
                .subject("Other Subject")
                .templateName("other-template.html")
                .body("This is another email body.")
                .buttonLink("https://other-example.com")
                .buttonText("Other button")
                .build();

        // Test Equals
        assertThat(message1.equals(message1)).isTrue();
        assertThat(message1.equals(message2)).isTrue();
        assertThat(message2.equals(message1)).isTrue();
        assertThat(message1.equals(message3)).isFalse();

        // Test HashCode
        assertThat(message1.hashCode()).isEqualTo(message1.hashCode());
        assertThat(message1.hashCode()).isEqualTo(message2.hashCode());
        assertThat(message1.hashCode()).isNotEqualTo(message3.hashCode());
    }

    @Test
    void testToString() {
        EmailMessage emailMessage = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();

        String expectedToString = "EmailMessage(to=recipient@example.com, subject=Test Subject, templateName=template.html, body=This is the email body., buttonLink=https://example.com, buttonText=Click me)";
        assertEquals(expectedToString, emailMessage.toString());
    }

    @Test
    void testReflexivity() {
        EmailMessage message1 = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();
                assertThat(message1.equals(message1)).isTrue();
    }

    @Test
    void testSymmetry() {
        EmailMessage message1 = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();

        EmailMessage message2 = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();

                assertThat(message1.equals(message2)).isTrue();
        assertThat(message2.equals(message1)).isTrue();
    }

    @Test
    void testTransitivity() {
        EmailMessage message1 = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();

        EmailMessage message2 = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();

        EmailMessage message3 = EmailMessage.builder()
                .to("other@example.com")
                .subject("Other Subject")
                .templateName("other-template.html")
                .body("This is another email body.")
                .buttonLink("https://other-example.com")
                .buttonText("Other button")
                .build();

                assertThat(message1.equals(message2)).isTrue();
        assertThat(message2.equals(message3)).isFalse();
        assertThat(message1.equals(message3)).isFalse();
    }

    @Test
    void testConsistency() {
        EmailMessage message1 = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();

        EmailMessage message2 = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();

                assertThat(message1.equals(message2)).isTrue();
    }

    @Test
    void testNullComparison() {
        EmailMessage message1 = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();
                assertThat(message1.equals(null)).isFalse();
    }

    @Test
    void testDifferentClassComparison() {
        EmailMessage message1 = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();
                assertThat(message1.equals("some string")).isFalse();
    }

    @Test
    void testHashCodeConsistency() {
        EmailMessage message1 = EmailMessage.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .templateName("template.html")
                .body("This is the email body.")
                .buttonLink("https://example.com")
                .buttonText("Click me")
                .build();

        int hashCode1 = message1.hashCode();
        int hashCode2 = message1.hashCode();
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

}
