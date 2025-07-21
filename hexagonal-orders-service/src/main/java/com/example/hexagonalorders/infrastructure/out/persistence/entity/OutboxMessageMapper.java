package com.example.hexagonalorders.infrastructure.out.persistence.entity;

import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.model.Status;

public class OutboxMessageMapper {
    
    public static OutboxJpaEntity toJpaEntity(OutboxMessage outboxMessage) {
        OutboxJpaEntity jpaEntity = new OutboxJpaEntity();
        jpaEntity.setId(outboxMessage.getId());
        jpaEntity.setAggregateType(outboxMessage.getAggregateType());
        jpaEntity.setAggregateId(outboxMessage.getAggregateId());
        jpaEntity.setEventType(outboxMessage.getEventType());
        jpaEntity.setPayload(outboxMessage.getPayload());
        jpaEntity.setStatus(mapToJpaStatus(outboxMessage.getStatus()));
        jpaEntity.setCreatedAt(outboxMessage.getCreatedAt());
        jpaEntity.setProcessedAt(outboxMessage.getProcessedAt());
        return jpaEntity;
    }
    
    public static OutboxMessage toDomainModel(OutboxJpaEntity jpaEntity) {
        return new OutboxMessage(
            jpaEntity.getId(),
            jpaEntity.getAggregateType(),
            jpaEntity.getAggregateId(),
            jpaEntity.getEventType(),
            jpaEntity.getPayload(),
            mapToDomainStatus(jpaEntity.getStatus()),
            jpaEntity.getCreatedAt(),
            jpaEntity.getProcessedAt()
        );
    }
    
    private static OutboxJpaEntity.OutboxStatusJpa mapToJpaStatus(Status domainStatus) {
        return switch (domainStatus) {
            case PENDING -> OutboxJpaEntity.OutboxStatusJpa.PENDING;
            case PROCESSING -> OutboxJpaEntity.OutboxStatusJpa.PROCESSING;
            case PROCESSED -> OutboxJpaEntity.OutboxStatusJpa.PROCESSED;
            case FAILED -> OutboxJpaEntity.OutboxStatusJpa.FAILED;
        };
    }
    
    private static Status mapToDomainStatus(OutboxJpaEntity.OutboxStatusJpa jpaStatus) {
        return switch (jpaStatus) {
            case PENDING -> Status.PENDING;
            case PROCESSING -> Status.PROCESSING;
            case PROCESSED -> Status.PROCESSED;
            case FAILED -> Status.FAILED;
        };
    }
} 