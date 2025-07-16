package com.example.hexagonalorders.infrastructure.out.persistence.mapper;

import com.example.hexagonalorders.domain.model.Delivery;
import com.example.hexagonalorders.domain.model.DeliveryStatus;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryId;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryAddress;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryDate;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.DeliveryEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeliveryPersistenceMapper {
    
    public Delivery toDomain(DeliveryEntity entity) {
        if (entity == null) {
            return null;
        }
        
        // Crear la dirección de entrega usando los campos de la entidad
        // Nota: La entidad no tiene state, así que usamos un valor por defecto
        DeliveryAddress deliveryAddress = new DeliveryAddress(
            entity.getStreet() != null ? entity.getStreet() : "Dirección por defecto",
            entity.getCity() != null ? entity.getCity() : "Ciudad",
            "Estado por defecto", // Valor por defecto para state
            entity.getPostalCode() != null ? entity.getPostalCode() : "12345",
            entity.getCountry() != null ? entity.getCountry() : "País"
        );
        
        // Crear la fecha de entrega
        DeliveryDate scheduledDate = new DeliveryDate(entity.getScheduledDate());
        
        return new Delivery(
            new DeliveryId(entity.getDeliveryId()),
            entity.getOrderNumber(),
            deliveryAddress,
            scheduledDate,
            entity.getStatus(),
            "Notas de entrega por defecto" // Valor por defecto para deliveryNotes
        );
    }
    
    public DeliveryEntity toEntity(Delivery delivery) {
        if (delivery == null) {
            return null;
        }
        
        DeliveryEntity entity = new DeliveryEntity(
            delivery.getDeliveryId().value(),
            delivery.getOrderNumber(),
            null, // routeId - no disponible en el dominio
            null, // deliveryPersonId - no disponible en el dominio
            delivery.getDeliveryAddress().getStreet(),
            delivery.getDeliveryAddress().getCity(),
            delivery.getDeliveryAddress().getPostalCode(),
            delivery.getDeliveryAddress().getCountry(),
            delivery.getStatus(),
            delivery.getScheduledDate().value()
        );
        
        return entity;
    }
    
    public List<Delivery> toDomainList(List<DeliveryEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        
        return entities.stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
} 