package com.example.hexagonalorders.infrastructure.out.persistence.repository;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OrderItem;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OrderJpaEntity;
import com.example.hexagonalorders.infrastructure.out.persistence.mapper.OrderJpaMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderRepositoryAdapter implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderJpaMapper orderJpaMapper;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository, OrderJpaMapper orderJpaMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderJpaMapper = orderJpaMapper;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity savedEntity = orderJpaRepository.save(orderJpaMapper.toJpaEntity(order));
        return orderJpaMapper.toDomain(savedEntity);
    }
    
    public OrderWithId saveWithId(Order order) {
        System.out.println("[DEBUG] Guardando orden con número: " + order.getOrderNumber().value());
        OrderJpaEntity savedEntity = orderJpaRepository.save(orderJpaMapper.toJpaEntity(order));
        return new OrderWithId(orderJpaMapper.toDomain(savedEntity), savedEntity.getId());
    }

    @Override
    public Optional<Order> findByOrderNumber(OrderNumber orderNumber) {
        return orderJpaRepository.findByOrderNumber(orderNumber.value())
                .map(orderJpaMapper::toDomain);
    }
    
    public Optional<OrderWithId> findByOrderNumberWithId(OrderNumber orderNumber) {
        return orderJpaRepository.findByOrderNumber(orderNumber.value())
                .map(entity -> new OrderWithId(orderJpaMapper.toDomain(entity), entity.getId()));
    }

    @Override
    public List<Order> findAll() {
        return orderJpaRepository.findAll().stream()
                .map(orderJpaMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    public List<OrderWithId> findAllWithId() {
        return orderJpaRepository.findAll().stream()
                .map(entity -> new OrderWithId(orderJpaMapper.toDomain(entity), entity.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByOrderNumber(OrderNumber orderNumber) {
        orderJpaRepository.deleteByOrderNumber(orderNumber.value());
    }
    
    public static class OrderWithId {
        private final Order order;
        private final Long id;
        
        public OrderWithId(Order order, Long id) {
            this.order = order;
            this.id = id;
        }
        
        public Order getOrder() {
            return order;
        }
        
        public Long getId() {
            return id;
        }
    }
} 