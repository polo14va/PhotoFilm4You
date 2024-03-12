package edu.uoc.epcsd.user.entities;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
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
