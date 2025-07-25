package com.example.hexagonalorders.domain.model;

import com.example.hexagonalorders.domain.event.DomainEvent;
import com.example.hexagonalorders.domain.event.OrderCreatedEvent;
import com.example.hexagonalorders.domain.event.OrderItemAddedEvent;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.Address;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Core domain entity representing an Order in the system.
 * This class is part of the domain layer and contains the business logic and rules
 * related to orders. It is independent of any infrastructure concerns.
 * 
 * An Order consists of:
 * - A unique identifier
 * - An order number (as a value object)
 * - A customer ID (reference to Customer aggregate)
 * - A creation date
 * - A list of order items
 * - A list of domain events
 */
public class Order {
    private Long id;
    private final OrderNumber orderNumber;
    private final String customerId;
    private final Address address;
    private final LocalDateTime orderDate;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Order(Long id, OrderNumber orderNumber, String customerId, Address address, LocalDateTime orderDate, List<OrderItem> items, OrderStatus status) {
        this(orderNumber, customerId, address, orderDate, items, status);
        this.id = id;
    }

    public Order(OrderNumber orderNumber, String customerId, Address address, LocalDateTime orderDate, List<OrderItem> items, OrderStatus status) {
        if (orderNumber == null) {
            throw new IllegalArgumentException("Order number cannot be null");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        if (orderDate == null) {
            throw new IllegalArgumentException("Order date cannot be null");
        }
        if (items == null) {
            throw new IllegalArgumentException("Items cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.address = address;
        this.orderDate = orderDate;
        this.items = items;
        this.status = status;
        // Add domain event for order creation
        domainEvents.add(new OrderCreatedEvent(null, orderNumber));
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Address getAddress() {
        return address;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    /**
     * Adds an item to the order. Business rules (e.g., no duplicates, min/max quantity, etc.)
     * should be enforced here.
     * Raises an OrderItemAddedEvent.
     */
    public void addItem(OrderItem item, Long orderId, Long itemId) {
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }
        // TODO: Enforce business rules here (e.g., no duplicate products, min/max quantity, etc.)
        this.items.add(item);
        domainEvents.add(new OrderItemAddedEvent(orderId, itemId, item.getProductNumber(), item.getQuantity()));
    }

    /**
     * Removes an item from the order. Business rules (e.g., order must have at least one item)
     * should be enforced here.
     */
    public void removeItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }
        // TODO: Enforce business rules here (e.g., order must have at least one item)
        this.items.remove(item);
    }

    /**
     * Confirma la orden, cambiando su estado a CONFIRMED y generando el evento de dominio.
     */
    public void confirm() {
        if (this.status == OrderStatus.CONFIRMED) {
            throw new IllegalStateException("La orden ya está confirmada");
        }
        this.status = OrderStatus.CONFIRMED;
        domainEvents.add(new com.example.hexagonalorders.domain.event.OrderConfirmedEvent(this.orderNumber));
    }

    // equals, hashCode, and toString can be added as needed
} 