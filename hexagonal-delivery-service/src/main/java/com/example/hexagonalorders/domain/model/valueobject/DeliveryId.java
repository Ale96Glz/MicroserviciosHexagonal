package com.example.hexagonalorders.domain.model.valueobject;
import java.util.Objects;

/**
 * Value Object que representa el identificador de una entrega.
 * Este objeto asegura la seguridad de tipo y encapsula la lógica del ID de entrega.
 */
public class DeliveryId {
    private final String value;

    public DeliveryId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryId that = (DeliveryId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "DeliveryId{" + "value='" + value + '\'' + '}';
    }
} 