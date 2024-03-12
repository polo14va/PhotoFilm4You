package edu.uoc.epcsd.productcatalog.infrastructure.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KafkaTopicConfigTest {
    private KafkaAdmin kafkaAdmin;
    private NewTopic unitAvailableTopic;
    private KafkaTopicConfig kafkaTopicConfig;

    @BeforeEach
    void setUp() {
        kafkaTopicConfig = new KafkaTopicConfig();
        kafkaAdmin = mock(KafkaAdmin.class);
        unitAvailableTopic = mock(NewTopic.class);

    }

    @Test
    void testKafkaAdmin() {
        // Arrange
        String bootstrapAddress = null;
        // Act
        KafkaAdmin kafkaAdmin = kafkaTopicConfig.kafkaAdmin();

        // Assert
        Map<String, Object> expectedConfigs = new HashMap<>();
        expectedConfigs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);

        assertEquals(expectedConfigs, kafkaAdmin.getConfigurationProperties());
    }

    @Test
    void testUnitAvailableTopic() {
        // Arrange
        NewTopic expectedTopic = new NewTopic("product.unit_available", 1, (short) 1);
        when(unitAvailableTopic.name()).thenReturn(expectedTopic.name());
        when(unitAvailableTopic.numPartitions()).thenReturn(expectedTopic.numPartitions());
        when(unitAvailableTopic.replicationFactor()).thenReturn(expectedTopic.replicationFactor());

        // Act
        NewTopic actualTopic = kafkaTopicConfig.unitAvailableTopic();

        // Assert
        assertNotNull(actualTopic);
        assertEquals(expectedTopic.name(), actualTopic.name());
        assertEquals(expectedTopic.numPartitions(), actualTopic.numPartitions());
        assertEquals(expectedTopic.replicationFactor(), actualTopic.replicationFactor());
    }
}