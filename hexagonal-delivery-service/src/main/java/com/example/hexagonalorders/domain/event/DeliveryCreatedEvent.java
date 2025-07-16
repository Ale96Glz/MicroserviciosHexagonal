package com.example.hexagonalorders.domain.event;
/**
 * Evento de dominio que se genera cuando se crea una nueva entrega.
 * Este evento representa un hecho significativo en el dominio de entregas.
 */
public class DeliveryCreatedEvent extends DomainEvent {
    private final String deliveryId;
    private final String orderNumber;

    public DeliveryCreatedEvent(String deliveryId, String orderNumber) {
        this.deliveryId = deliveryId;
        this.orderNumber = orderNumber;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
} 