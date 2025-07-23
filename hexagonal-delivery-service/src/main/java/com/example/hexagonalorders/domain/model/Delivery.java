package com.example.hexagonalorders.domain.model;

import com.example.hexagonalorders.domain.event.DomainEvent;
import com.example.hexagonalorders.domain.event.DeliveryCreatedEvent;
import com.example.hexagonalorders.domain.event.DeliveryStatusChangedEvent;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryId;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryAddress;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryDate;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryItem;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Aggregate root representing a Delivery in the delivery domain.
 * This class encapsulates the business logic and rules related to deliveries.
 * 
 * A Delivery consists of:
 * - A unique delivery identifier
 * - An associated order number
 * - Delivery address information
 * - Scheduled delivery date
 * - Current delivery status
 * - Delivery notes
 * - Domain events
 */
public class Delivery {
    private final DeliveryId deliveryId;
    private String orderNumber;
    private final DeliveryAddress deliveryAddress;
    private DeliveryDate scheduledDate;
    private DeliveryStatus status;
    private String deliveryNotes;
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    private final List<DeliveryItem> items;

    public Delivery(DeliveryId deliveryId, String orderNumber, DeliveryAddress deliveryAddress, 
                   DeliveryDate scheduledDate, DeliveryStatus status, String deliveryNotes, List<DeliveryItem> items) {
        if (deliveryId == null) {
            throw new IllegalArgumentException("Delivery ID cannot be null");
        }
        if (orderNumber == null) {
            throw new IllegalArgumentException("Order number cannot be null");
        }
        if (deliveryAddress == null) {
            throw new IllegalArgumentException("Delivery address cannot be null");
        }
        if (scheduledDate == null) {
            throw new IllegalArgumentException("Scheduled date cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Delivery status cannot be null");
        }
        if (items == null) {
            throw new IllegalArgumentException("Delivery items cannot be null");
        }
        this.deliveryId = deliveryId;
        this.orderNumber = orderNumber;
        this.deliveryAddress = deliveryAddress;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.deliveryNotes = deliveryNotes;
        this.items = new ArrayList<>(items);
        // Add evento de dominio para la creación de la entrega
        domainEvents.add(new DeliveryCreatedEvent(deliveryId.value(), orderNumber));
    }

    // Getters
    public DeliveryId getDeliveryId() {
        return deliveryId;
    }
    public String getOrderNumber() {
        return orderNumber;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public DeliveryDate getScheduledDate() {
        return scheduledDate;
    }

    public DeliveryStatus getStatus() {
        return status;
    }
    public String getDeliveryNotes() {
        return deliveryNotes;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    public List<DeliveryItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    /**
     * Schedule para la entrega
     * Representa la acción de programar una entrega.
     */
    public void scheduleDelivery(DeliveryDate newScheduledDate) {
        if (newScheduledDate == null) {
            throw new IllegalArgumentException("Scheduled date cannot be null");
        }
        
        if (status == DeliveryStatus.CANCELLED) {
            throw new IllegalStateException("Cannot schedule a cancelled delivery");
        }
        
        this.scheduledDate = newScheduledDate;
        this.status = DeliveryStatus.SCHEDULED;
        
        domainEvents.add(new DeliveryStatusChangedEvent(deliveryId.value(), status));
    }

    /**
     * Confirma la entrega para el pick up.
     * Representa la acción de confirmar la preparación de la entrega.
     */
    public void confirmDelivery() {
        if (status == DeliveryStatus.CANCELLED) {
            throw new IllegalStateException("Cannot confirm a cancelled delivery");
        }
        
        this.status = DeliveryStatus.CONFIRMED;
        domainEvents.add(new DeliveryStatusChangedEvent(deliveryId.value(), status));
    }

    /**
     * Marca la entrega como en tránsito.
     * Representa la acción de iniciar el proceso de entrega.
     */
    public void startDelivery() {
        if (status != DeliveryStatus.CONFIRMED) {
            throw new IllegalStateException("Delivery must be confirmed before starting");
        }
        
        this.status = DeliveryStatus.IN_TRANSIT;
        domainEvents.add(new DeliveryStatusChangedEvent(deliveryId.value(), status));
    }

    /**
         * Marks the delivery as completed.
         * This represents the business action of completing the delivery.
     */
    public void completeDelivery() {
        if (status != DeliveryStatus.IN_TRANSIT) {
            throw new IllegalStateException("Delivery must be in transit before completing");
        }
        
        this.status = DeliveryStatus.COMPLETED;
        domainEvents.add(new DeliveryStatusChangedEvent(deliveryId.value(), status));
    }

    /**
         * Cancels the delivery.
         * This represents the business action of cancelling a delivery.
     */
    public void cancelDelivery() {
        if (status == DeliveryStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed delivery");
        }
        
        this.status = DeliveryStatus.CANCELLED;
        domainEvents.add(new DeliveryStatusChangedEvent(deliveryId.value(), status));
    }

    /**
     * Actualiza las notas de la entrega.
     * Representa la acción de actualizar las notas de la entrega.
     */
    public void updateDeliveryNotes(String notes) {
        this.deliveryNotes = notes;
    }

    /**
     * Revisa si la entrega puede ser cancelada.
     */
    public boolean canBeCancelled() {
        return status != DeliveryStatus.COMPLETED && status != DeliveryStatus.CANCELLED;
    }

    /**
     * Revisa si la entrega está activa (no cancelada o completada).
     */
    public boolean isActive() {
        return status != DeliveryStatus.CANCELLED && status != DeliveryStatus.COMPLETED;
    }
} 