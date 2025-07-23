package com.example.hexagonalorders.infrastructure.out.persistence.mapper;

import com.example.hexagonalorders.domain.model.Delivery;
import com.example.hexagonalorders.domain.model.DeliveryStatus;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryId;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryAddress;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryDate;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.DeliveryEntity;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.DeliveryItemEntity;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryItem;
import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class DeliveryPersistenceMapper {
    
    public Delivery toDomain(DeliveryEntity entity) {
        if (entity == null) {
            return null;
        }
        
        // Crear la dirección de entrega usando los campos de la entidad
        DeliveryAddress deliveryAddress = new DeliveryAddress(
            entity.getStreet() != null ? entity.getStreet() : "Dirección por defecto",
            entity.getCity() != null ? entity.getCity() : "Ciudad",
            "Estado por defecto", // Valor por defecto para state
            entity.getPostalCode() != null ? entity.getPostalCode() : "12345",
            entity.getCountry() != null ? entity.getCountry() : "País"
        );
        
        // Crear la fecha de entrega
        DeliveryDate scheduledDate = new DeliveryDate(entity.getScheduledDate());
        
        List<DeliveryItem> items = entity.getItems() != null ? entity.getItems().stream()
            .map(itemEntity -> new DeliveryItem(
                new ProductNumber(itemEntity.getProductNumber()),
                new Quantity(itemEntity.getQuantity())
            )).collect(Collectors.toList()) : new ArrayList<>();
        
        return new Delivery(
            new DeliveryId(entity.getDeliveryId()),
            entity.getOrderNumber(),
            deliveryAddress,
            scheduledDate,
            entity.getStatus(),
            "Notas de entrega por defecto",
            items
        );
    }
    
    public DeliveryEntity toEntity(Delivery delivery) {
        if (delivery == null) {
            return null;
        }
        
        DeliveryEntity entity = new DeliveryEntity(
            delivery.getDeliveryId().value(),
            delivery.getOrderNumber(),
            null, // routeId - no disponible en el dominio de momento
            null, // deliveryPersonId - no disponible en el dominio de momento
            delivery.getDeliveryAddress().getStreet(),
            delivery.getDeliveryAddress().getCity(),
            delivery.getDeliveryAddress().getPostalCode(),
            delivery.getDeliveryAddress().getCountry(),
            delivery.getStatus(),
            delivery.getScheduledDate().value()
        );
        
        // Mapear los ítems de dominio a entidades
        List<DeliveryItemEntity> itemEntities = delivery.getItems() != null ? delivery.getItems().stream()
            .map(item -> new DeliveryItemEntity(
                item.getProductNumber().value(),
                item.getQuantity().value(),
                entity
            )).collect(Collectors.toList()) : new ArrayList<>();
        entity.setItems(itemEntities);
        
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