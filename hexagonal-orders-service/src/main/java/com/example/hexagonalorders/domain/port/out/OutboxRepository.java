package com.example.hexagonalorders.domain.port.out;

import com.example.hexagonalorders.domain.model.OutboxMessage;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository {
    
    void save(OutboxMessage outboxMessage);
    
    List<OutboxMessage> findPending(int limit);
    
    /**
     * Finds all pending outbox messages for processing.
     * 
     * @return list of pending outbox messages
     */
    List<OutboxMessage> findPendingMessages();
    
    void markProcessed(UUID id);
    
    void markFailed(UUID id);
} 