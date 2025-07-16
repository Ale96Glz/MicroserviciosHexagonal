package com.example.hexagonalorders.domain.model.valueobject;

public record DeliveryPersonId(String value) {
    public DeliveryPersonId {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del repartidor no puede ser nulo o vacio");
        }
    }

    @Override
    public String toString() {
        return value;
    }
} 