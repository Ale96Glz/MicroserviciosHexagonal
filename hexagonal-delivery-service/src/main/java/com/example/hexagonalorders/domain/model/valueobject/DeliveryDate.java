package com.example.hexagonalorders.domain.model.valueobject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object que representa la fecha y hora de una entrega.
 * Este objeto encapsula la fecha programada de entrega con reglas de negocio.
 */
public class DeliveryDate {
    private final LocalDateTime value;

    public DeliveryDate(LocalDateTime value) {
        if (value == null) {
            throw new IllegalArgumentException("Delivery date cannot be null");
        }
        
        // Business rule: Delivery date cannot be in the past
        if (value.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Delivery date cannot be in the past");
        }
        
        this.value = value;
    }

    public LocalDateTime value() {
        return value;
    }

    public boolean isInThePast() {
        return value.isBefore(LocalDateTime.now());
    }

    public boolean isToday() {
        LocalDateTime now = LocalDateTime.now();
        return value.toLocalDate().equals(now.toLocalDate());
    }

    public boolean isInTheFuture() {
        return value.isAfter(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryDate that = (DeliveryDate) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "DeliveryDate{" + "value=" + value + '}';
    }
} 