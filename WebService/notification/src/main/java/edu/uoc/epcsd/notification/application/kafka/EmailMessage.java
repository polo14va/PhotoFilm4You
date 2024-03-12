package edu.uoc.epcsd.notification.application.kafka;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {
    private String to;
    private String subject;
    private String templateName;
    private String body;
    private String buttonLink;
    private String buttonText;

}
