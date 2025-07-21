package com.example.hexagonalorders.application.service;

import com.example.hexagonalorders.domain.event.DomainEvent;
import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.port.in.OrderUseCase;
import com.example.hexagonalorders.domain.port.out.OrderNumberGenerator;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import com.example.hexagonalorders.domain.service.OrderValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service implementing the order-related use cases.
 * This class orchestrates domain logic and coordinates with output ports.
 * It is part of the application layer and is responsible for:
 * - Implementing use cases defined by the domain
 * - Coordinating between domain objects and services
 * - Managing transactions and use case flow
 */
@Service
// @RequiredArgsConstructor
public class OrderService implements OrderUseCase {
    private final OrderRepository orderRepository;
    private final OrderNumberGenerator orderNumberGenerator;
    private final OrderValidationService orderValidationService;
    private final ApplicationEventPublisher eventPublisher;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository, 
                       OrderNumberGenerator orderNumberGenerator, 
                       OrderValidationService orderValidationService, 
                       ApplicationEventPublisher eventPublisher, 
                       OutboxRepository outboxRepository, 
                       ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.orderNumberGenerator = orderNumberGenerator;
        this.orderValidationService = orderValidationService;
        this.eventPublisher = eventPublisher;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public Order createOrder(com.example.hexagonalorders.infrastructure.in.web.mapper.OrderMapper.OrderCreationData orderData) {
        OrderNumber orderNumber = orderNumberGenerator.generate();
        System.out.println("[DEBUG] Creando orden con número: " + orderNumber.value());

        // Crear la orden con el número generado y los datos recibidos
        Order order = new Order(
            orderNumber,
            orderData.getCustomerId(),
            orderData.getOrderDate(),
            orderData.getItems(),
            orderData.getStatus()
        );

        // Validar la orden usando el servicio de dominio
        orderValidationService.validateOrder(order);

        // Usar el método que devuelve el id
        var orderWithId = ((com.example.hexagonalorders.infrastructure.out.persistence.repository.OrderRepositoryAdapter) orderRepository).saveWithId(order);
        Order savedOrder = orderWithId.getOrder();

        // Procesar eventos de dominio: publicar internamente y persistir en outbox
        for (DomainEvent event : savedOrder.getDomainEvents()) {
            eventPublisher.publishEvent(event);
            persistToOutbox(event, "Order", orderNumber.value());
        }

        savedOrder.clearDomainEvents();

        return savedOrder;
    }
    
    public com.example.hexagonalorders.infrastructure.out.persistence.repository.OrderRepositoryAdapter.OrderWithId createOrderWithId(com.example.hexagonalorders.infrastructure.in.web.mapper.OrderMapper.OrderCreationData orderData) {
        OrderNumber orderNumber = orderNumberGenerator.generate();
        System.out.println("[DEBUG] Creando orden con número: " + orderNumber.value());

        // Crear la orden con el número generado y los datos recibidos
        Order order = new Order(
            orderNumber,
            orderData.getCustomerId(),
            orderData.getOrderDate(),
            orderData.getItems(),
            orderData.getStatus()
        );

        // Validar la orden usando el servicio de dominio
        orderValidationService.validateOrder(order);

        // Usar el método que devuelve el id
        var orderWithId = ((com.example.hexagonalorders.infrastructure.out.persistence.repository.OrderRepositoryAdapter) orderRepository).saveWithId(order);
        Order savedOrder = orderWithId.getOrder();

        // Procesar eventos de dominio: publicar internamente y persistir en outbox
        for (DomainEvent event : savedOrder.getDomainEvents()) {
            eventPublisher.publishEvent(event);
            persistToOutbox(event, "Order", orderNumber.value());
        }

        savedOrder.clearDomainEvents();

        return orderWithId;
    }

    @Override
    public Optional<Order> getOrder(OrderNumber orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
    
    public Optional<com.example.hexagonalorders.infrastructure.out.persistence.repository.OrderRepositoryAdapter.OrderWithId> getOrderWithId(OrderNumber orderNumber) {
        return ((com.example.hexagonalorders.infrastructure.out.persistence.repository.OrderRepositoryAdapter) orderRepository)
            .findByOrderNumberWithId(orderNumber);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteOrder(OrderNumber orderNumber) {
        orderRepository.deleteByOrderNumber(orderNumber);
    }

    @Override
    @Transactional
    public void confirmOrder(OrderNumber orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + orderNumber.value()));
        System.out.println("[DEBUG] Estado antes de confirmar: " + order.getStatus());
        order.confirm();
        System.out.println("[DEBUG] Estado después de confirmar: " + order.getStatus());
        orderRepository.save(order);
        for (DomainEvent event : order.getDomainEvents()) {
            eventPublisher.publishEvent(event);
            persistToOutbox(event, "Order", orderNumber.value());
        }
        order.clearDomainEvents();
    }

    /**
     * Persists a domain event to the outbox.
     *
     * @param event the domain event
     * @param aggregateType the type of aggregate that produced the event
     * @param aggregateId the identifier of the aggregate
     */
    protected void persistToOutbox(DomainEvent event, String aggregateType, String aggregateId) {
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
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist event to outbox", e);
        }
    }
} 