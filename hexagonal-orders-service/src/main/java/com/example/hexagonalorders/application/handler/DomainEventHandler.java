package com.example.hexagonalorders.application.handler;

import com.example.hexagonalorders.application.event.OrderConfirmedIntegrationEvent;
import com.example.hexagonalorders.domain.event.DomainEvent;
import com.example.hexagonalorders.domain.event.OrderConfirmedEvent;
import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.valueobject.Address;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

/**
* Event handler for domain events that need to be mapped to integration events.
* This class centralizes the business logic for deciding which domain events
* should trigger external integration events.
*/
@Component
public class DomainEventHandler {
  
   private static final Logger log = LoggerFactory.getLogger(DomainEventHandler.class);
   
   private final OutboxRepository outboxRepository;
   private final ObjectMapper objectMapper;
   private final OrderRepository orderRepository;

   public DomainEventHandler(OutboxRepository outboxRepository, ObjectMapper objectMapper, OrderRepository orderRepository) {
       this.outboxRepository = outboxRepository;
       this.objectMapper = objectMapper;
       this.orderRepository = orderRepository;
   }
  
   /**
    * Handles OrderConfirmedEvent by creating and persisting the corresponding integration event.
    * This method is automatically called by Spring's event system when an OrderConfirmedEvent is published.
    *
    * @param event the OrderConfirmedEvent that was raised
    */
   @EventListener
   public void handleOrderConfirmed(OrderConfirmedEvent event) {
       log.debug("DOMAIN EVENT HANDLER: Handling OrderConfirmedEvent for order: {}", event.getOrderNumber());
      
       try {
           // Obtener la orden para extraer la dirección
           Order order = orderRepository.findByOrderNumber(event.getOrderNumber())
               .orElseThrow(() -> new IllegalArgumentException("Order not found for event: " + event.getOrderNumber().value()));
           Address address = order.getAddress();
           // Crear lista de ítems para el evento
           java.util.List<OrderConfirmedIntegrationEvent.ItemDto> items = order.getItems().stream()
               .map(i -> new OrderConfirmedIntegrationEvent.ItemDto(
                   i.getProductNumber().value(),
                   i.getQuantity().value(),
                   i.getUnitPrice().doubleValue()
               )).collect(Collectors.toList());
           // Create integration event
           OrderConfirmedIntegrationEvent integrationEvent =
               new OrderConfirmedIntegrationEvent(event.getOrderNumber(),
                   address != null ? address.street() : null,
                   address != null ? address.city() : null,
                   address != null ? address.postalCode() : null,
                   address != null ? address.country() : null,
                   items);
          
           // Persist to outbox for reliable delivery
           persistToOutbox(integrationEvent, "Order", event.getOrderNumber().value());
          
           log.debug("DOMAIN EVENT HANDLER: Successfully created integration event for order confirmation: {}",
                    event.getOrderNumber());
       } catch (Exception e) {
           log.debug("DOMAIN EVENT HANDLER: Failed to handle OrderConfirmedEvent for order: {}",
                    event.getOrderNumber(), e);
           throw new RuntimeException("Failed to process order confirmation integration event", e);
       }
   }
  
   /**
    * Generic handler for domain events that don't require integration events.
    * This method logs the event for debugging purposes but doesn't create integration events.
    *
    * @param event any domain event that doesn't have a specific handler
    */
   @EventListener
   public void handleGenericDomainEvent(DomainEvent event) {
       log.debug("DOMAIN EVENT HANDLER: Received domain event: {} - no integration event required",
                event.getClass().getSimpleName());
   }
  
   /**
    * Persists an integration event to the outbox.
    *
    * @param event the integration event to persist
    * @param aggregateType the type of aggregate that produced the event
    * @param aggregateId the identifier of the aggregate
    */
   private void persistToOutbox(Object event, String aggregateType, String aggregateId) {
       try {
           String payload = objectMapper.writeValueAsString(event);
           String eventType = event.getClass().getSimpleName();
          
           // Generate a deterministic UUID based on the aggregateId string
           // This ensures the same aggregate always gets the same UUID
           UUID uuid = UUID.nameUUIDFromBytes(aggregateId.getBytes());
          
           OutboxMessage outboxMessage = OutboxMessage.createPendingMessage(
               aggregateType,
               uuid,
               eventType,
               payload
           );
          
           outboxRepository.save(outboxMessage);
           log.debug("Persisted integration event to outbox: {} for aggregate: {}",
                    eventType, aggregateId);
       } catch (Exception e) {
           log.error("Failed to persist integration event to outbox for aggregate: {}",
                    aggregateId, e);
           throw new RuntimeException("Failed to persist event to outbox", e);
       }
   }
} 