package edu.uoc.epcsd.notification.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
@ComponentScan(basePackages = "edu.uoc.epcsd.notification")
public class NotificationConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}



