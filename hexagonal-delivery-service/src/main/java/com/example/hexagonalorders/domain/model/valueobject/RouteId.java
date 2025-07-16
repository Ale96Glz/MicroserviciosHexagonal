package com.example.hexagonalorders.domain.model.valueobject;

public record RouteId(String value) {
    public RouteId {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la ruta no puede ser nulo o vacio");
        }
    }

    @Override
    public String toString() {
        return value;
    }
} 