package com.example.hexagonalorders.domain.model;

import java.time.Instant;
import java.util.UUID;

public class OutboxMessage {
    private UUID id;
    private String aggregateType;
    private UUID aggregateId;
    private String eventType;
    private String payload;
    private Status status;
    private Instant createdAt;
    private Instant processedAt;
    private String errorMessage;

    public OutboxMessage(UUID id, String aggregateType, UUID aggregateId, String eventType, 
                        String payload, Status status, Instant createdAt, Instant processedAt) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = status;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
    }
    
    public static OutboxMessage createPendingMessage(String aggregateType, UUID aggregateId, String eventType, String payload) {
        return new OutboxMessage(
            UUID.randomUUID(),
            aggregateType,
            aggregateId,
            eventType,
            payload,
            Status.PENDING,
            Instant.now(),
            null
        );
    }

    // Getters
    public UUID getId() { return id; }
    public String getAggregateType() { return aggregateType; }
    public UUID getAggregateId() { return aggregateId; }
    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public Status getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getProcessedAt() { return processedAt; }
    public String getErrorMessage() { return errorMessage; }

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setAggregateType(String aggregateType) { this.aggregateType = aggregateType; }
    public void setAggregateId(UUID aggregateId) { this.aggregateId = aggregateId; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setPayload(String payload) { this.payload = payload; }
    public void setStatus(Status status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setProcessedAt(Instant processedAt) { this.processedAt = processedAt; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    // Business methods
    public void markAsProcessing() {
        this.status = Status.PROCESSING;
    }

    public void markAsProcessed() {
        this.status = Status.PROCESSED;
        this.processedAt = Instant.now();
    }

    public void markAsFailed(String errorMessage) {
        this.status = Status.FAILED;
        this.errorMessage = errorMessage;
        this.processedAt = Instant.now();
    }
} 