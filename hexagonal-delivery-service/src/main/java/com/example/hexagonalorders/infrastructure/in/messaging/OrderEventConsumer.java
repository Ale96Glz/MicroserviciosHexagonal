package com.example.hexagonalorders.infrastructure.in.messaging;

import com.example.hexagonalorders.application.service.DeliveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor de eventos de integración relacionados con órdenes.
 * Este componente escucha tópicos de Kafka y procesa eventos de órdenes
 * para activar la lógica de negocio relacionada con entregas.
 */
@Component
public class OrderEventConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);
    
    private final DeliveryService deliveryService;
    private final ObjectMapper objectMapper;
    
    public OrderEventConsumer(DeliveryService deliveryService, ObjectMapper objectMapper) {
        this.deliveryService = deliveryService;
        this.objectMapper = objectMapper;
    }
    
    /**
     * Escucha eventos de órdenes confirmadas y crea entregas.
     * Este método procesa mensajes OrderConfirmedIntegrationEvent.
     */
    @KafkaListener(
        topics = "${kafka.topic.order-confirmed:hexagonal-orders-dev-order-confirmed}",
        groupId = "${kafka.consumer.group-id:delivery-service-group}"
    )
    public void handleOrderConfirmed(String message) {
        try {
            logger.info("Evento de orden confirmada recibido: {}", message);
            
            // Analizar el evento de integración
            OrderConfirmedIntegrationEvent event = objectMapper.readValue(
                message, OrderConfirmedIntegrationEvent.class);
            
            // Crear entrega para la orden confirmada
            deliveryService.createDeliveryFromOrder(
                event.getOrderNumber(),
                event.getStreet(),
                event.getCity(),
                event.getPostalCode(),
                event.getCountry(),
                event.getItems()
            );
            
            logger.info("Evento de orden confirmada procesado exitosamente para la orden: {}", 
                event.getOrderNumber());
            
        } catch (Exception e) {
            logger.error("Error procesando evento de orden confirmada: {}", message, e);
            // Considerar implementar cola de mensajes muertos o mecanismo de reintento
        }
    }
    
    /**
     * DTO de evento de integración para confirmación de orden.
     * Representa la estructura del evento de integración.
     */
    public static class OrderConfirmedIntegrationEvent {
        private String street;
        private String city;
        private String postalCode;
        private String country;
        private java.util.List<OrderItem> items;
        private String orderNumber;
        // Getters y setters
        public String getOrderNumber() { return orderNumber; }
        public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public java.util.List<OrderItem> getItems() { return items; }
        public void setItems(java.util.List<OrderItem> items) { this.items = items; }
    }
    
    /**
     * DTO de ítem de orden para eventos de integración.
     */
    public static class OrderItem {
        private String productNumber;
        private int quantity;
        
        // Getters y setters
        public String getProductNumber() { return productNumber; }
        public void setProductNumber(String productNumber) { this.productNumber = productNumber; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
} 