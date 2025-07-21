package com.example.hexagonalorders.domain.port.out;

/**
 * Output port for publishing messages to external systems.
 * This interface defines the contract for message publishing in the domain layer.
 */
public interface MessagePublisher {
    /**
     * Publishes a message to a specified topic.
     *
     * @param topic the topic to publish to (format: "aggregateType.eventType")
     * @param payload the message payload to publish
     */
    void publish(String topic, String payload);
} 