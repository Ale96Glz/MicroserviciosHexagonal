package com.example.hexagonalorders.domain.event;

import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;

public class OrderConfirmedEvent extends DomainEvent {
    private final OrderNumber orderNumber;

    public OrderConfirmedEvent(OrderNumber orderNumber) {
        super();
        this.orderNumber = orderNumber;
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }
} 