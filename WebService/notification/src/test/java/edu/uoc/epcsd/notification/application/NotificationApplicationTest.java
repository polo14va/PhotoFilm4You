package edu.uoc.epcsd.notification.application;

import edu.uoc.epcsd.notification.NotificationApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = NotificationApplication.class)
@ExtendWith(OutputCaptureExtension.class)
class NotificationApplicationTest {
    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        //if init all ok
        Assertions.assertNotNull(context);
    }

}