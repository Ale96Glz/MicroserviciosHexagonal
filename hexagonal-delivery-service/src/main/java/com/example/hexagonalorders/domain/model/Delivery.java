package com.example.hexagonalorders.domain.model;

import com.example.hexagonalorders.domain.event.DomainEvent;
import com.example.hexagonalorders.domain.event.DeliveryCreatedEvent;
import com.example.hexagonalorders.domain.event.DeliveryStatusChangedEvent;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryId;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryAddress;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryDate;


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

    public Delivery(DeliveryId deliveryId, String orderNumber, DeliveryAddress deliveryAddress, 
                   DeliveryDate scheduledDate, DeliveryStatus status, String deliveryNotes) {
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
        
        this.deliveryId = deliveryId;
        this.orderNumber = orderNumber;
        this.deliveryAddress = deliveryAddress;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.deliveryNotes = deliveryNotes;
        
        // Add domain event for delivery creation
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
    /**
     * Schedules the delivery for a specific date and time.
     * This represents the business action of scheduling a delivery.
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
     * Confirms the delivery is ready for pickup.
     * This represents the business action of confirming delivery preparation.
     */
    public void confirmDelivery() {
        if (status == DeliveryStatus.CANCELLED) {
            throw new IllegalStateException("Cannot confirm a cancelled delivery");
        }
        
        this.status = DeliveryStatus.CONFIRMED;
        domainEvents.add(new DeliveryStatusChangedEvent(deliveryId.value(), status));
    }

    /**
     * Marks the delivery as in transit.
     * This represents the business action of starting the delivery process.
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
     * Updates delivery notes.
     * This represents the business action of adding delivery information.
     */
    public void updateDeliveryNotes(String notes) {
        this.deliveryNotes = notes;
    }

    /**
     * Checks if the delivery can be cancelled.
     */
    public boolean canBeCancelled() {
        return status != DeliveryStatus.COMPLETED && status != DeliveryStatus.CANCELLED;
    }

    /**
     * Checks if the delivery is active (not cancelled or completed).
     */
    public boolean isActive() {
        return status != DeliveryStatus.CANCELLED && status != DeliveryStatus.COMPLETED;
    }
} 