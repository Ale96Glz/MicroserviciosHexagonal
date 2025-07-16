package com.example.hexagonalorders.infrastructure.out.persistence.repository;

import com.example.hexagonalorders.domain.model.Delivery;
import com.example.hexagonalorders.domain.model.DeliveryStatus;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryId;
import com.example.hexagonalorders.domain.port.out.DeliveryRepository;
import com.example.hexagonalorders.infrastructure.out.persistence.mapper.DeliveryPersistenceMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class DeliveryRepositoryAdapter implements DeliveryRepository {
    
    private final DeliveryJpaRepository jpaRepository;
    private final DeliveryPersistenceMapper mapper;
    
    public DeliveryRepositoryAdapter(DeliveryJpaRepository jpaRepository, DeliveryPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Delivery save(Delivery delivery) {
        var entity = mapper.toEntity(delivery);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Delivery> findById(DeliveryId deliveryId) {
        return jpaRepository.findByDeliveryId(deliveryId.value())
            .map(mapper::toDomain);
    }
    
    @Override
    public List<Delivery> findAll() {
        var entities = jpaRepository.findAll();
        return mapper.toDomainList(entities);
    }
    
    @Override
    public List<Delivery> findByStatus(DeliveryStatus status) {
        var entities = jpaRepository.findByStatus(status);
        return mapper.toDomainList(entities);
    }
    
    @Override
    public void deleteById(DeliveryId deliveryId) {
        jpaRepository.findByDeliveryId(deliveryId.value())
            .ifPresent(jpaRepository::delete);
    }
} 