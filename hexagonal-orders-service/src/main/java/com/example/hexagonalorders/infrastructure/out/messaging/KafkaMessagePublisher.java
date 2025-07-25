package com.example.hexagonalorders.infrastructure.out.messaging;

import com.example.hexagonalorders.domain.port.out.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
* Kafka implementation of MessagePublisher that publishes messages to Kafka topics.
* This implementation provides reliable message delivery with proper error handling
* and logging for the outbox pattern.
*/
@Primary
@Component
public class KafkaMessagePublisher implements MessagePublisher {

   private static final Logger log = LoggerFactory.getLogger(KafkaMessagePublisher.class);

   private final KafkaTemplate<String, String> kafkaTemplate;
   private final TopicNameMapper topicNameMapper;

    public KafkaMessagePublisher(KafkaTemplate<String, String> kafkaTemplate, TopicNameMapper topicNameMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicNameMapper = topicNameMapper;
    }

   @Override
   public void publish(String topic, String payload) {
       try {
           // Extract aggregate type and event type from the topic
           String[] topicParts = topic.split("\\.");
           if (topicParts.length != 2) {
               throw new IllegalArgumentException("Invalid topic format. Expected 'aggregateType.eventType', got: " + topic);
           }

           String aggregateType = topicParts[0];
           String eventType = topicParts[1];
          
           // Map to the actual Kafka topic name
           String kafkaTopic = topicNameMapper.mapToTopicName(aggregateType, eventType);
          
           log.debug("Publishing message to Kafka topic '{}' (original topic: '{}')", kafkaTopic, topic);
          
           // Use the event type as the key for partitioning
           CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(kafkaTopic, eventType, payload);
          
           // Handle the result asynchronously
           future.whenComplete((result, throwable) -> {
               if (throwable != null) {
                   log.error("Failed to publish message to Kafka topic '{}': {}", kafkaTopic, throwable.getMessage(), throwable);
               } else {
                   log.debug("Successfully published message to Kafka topic '{}' at partition {} offset {}",
                            kafkaTopic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
               }
           });
          
           log.info("Message sent to Kafka topic '{}' (aggregate: {}, event: {})", kafkaTopic, aggregateType, eventType);
          
       } catch (Exception e) {
           log.error("Failed to publish message to topic '{}': {}", topic, e.getMessage(), e);
           throw new RuntimeException("Failed to publish message to Kafka", e);
       }
   }
} 