package com.example.hexagonalorders.domain.event;

import com.example.hexagonalorders.domain.model.DeliveryStatus;
/**
 * Evento de dominio que se genera cuando cambia el estado de una entrega.
 * Este evento representa un hecho significativo en el dominio de entregas.
 */
public class DeliveryStatusChangedEvent extends DomainEvent {
    private final String deliveryId;
    private final DeliveryStatus newStatus;

    public DeliveryStatusChangedEvent(String deliveryId, DeliveryStatus newStatus) {
        this.deliveryId = deliveryId;
        this.newStatus = newStatus;
    }

    public String getDeliveryId() {
        return deliveryId;
    }
    public DeliveryStatus getNewStatus() {
        return newStatus;
    }
} 