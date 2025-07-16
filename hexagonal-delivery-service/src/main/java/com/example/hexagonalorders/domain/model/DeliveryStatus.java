package com.example.hexagonalorders.domain.model;
/**
 * Enum que representa los posibles estados de una entrega.
 * Estos estados representan el flujo de trabajo de una entrega.
 */
public enum DeliveryStatus {
    /**
     * Entrega creada pero no programada
     */
    CREATED,
    
    /**
     * Entrega programada para una fecha y hora específicas
     */
    SCHEDULED,
    
    /**
     * Entrega confirmada y lista para recoger
     */
    CONFIRMED,
    
    /**
     * Entrega actualmente en tránsito
     */
    IN_TRANSIT,
    
    /**
     * Entrega completada con éxito
     */
    COMPLETED,
    
    /**
     * Entrega cancelada
     */
    CANCELLED
} 