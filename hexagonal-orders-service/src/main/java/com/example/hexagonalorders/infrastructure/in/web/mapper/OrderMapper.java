package com.example.hexagonalorders.infrastructure.in.web.mapper;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OrderItem;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import com.example.hexagonalorders.domain.model.OrderStatus;
import com.example.hexagonalorders.domain.model.valueobject.Address;
import com.example.hexagonalorders.infrastructure.in.web.dto.OrderDto;
import com.example.hexagonalorders.infrastructure.in.web.dto.OrderItemDto;
import com.example.hexagonalorders.infrastructure.in.web.dto.OrderResponseDto;
import com.example.hexagonalorders.infrastructure.in.web.dto.AddressDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for converting between domain entities and DTOs.
 * This class is part of the adapter layer and helps maintain
 * the separation between the domain model and the API layer.
 */
@Component
public class OrderMapper {
    
    public OrderResponseDto toResponseDto(Order order, Long id) {
        if (order == null) {
            return null;
        }
        return new OrderResponseDto(order, id);
    }
    
    public Order toDomain(OrderDto dto) {
        if (dto == null) {
            return null;
        }
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(dto.getStatus().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Estado de orden inválido: " + dto.getStatus() + ". Valores permitidos: CREATED, PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED");
        }
        
        // Si el orderNumber es nulo o vacío, no crear la entidad Order aquí
        // El servicio se encargará de generar el número y crear la entidad
        if (dto.getOrderNumber() == null || dto.getOrderNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Para conversión completa, el orderNumber no puede ser nulo. Use createOrderData() para creación de nuevas órdenes.");
        }
        
        return new Order(
            new OrderNumber(dto.getOrderNumber()),
            dto.getCustomerId(),
            toDomainAddress(dto.getAddress()),
            dto.getOrderDate(),
            toDomainItems(dto.getItems()),
            status
        );
    }
    
    /**
     * Extrae los datos necesarios para crear una nueva orden.
     * Este método no requiere orderNumber ya que será generado por el servicio.
     */
    public OrderCreationData createOrderData(OrderDto dto) {
        if (dto == null) {
            return null;
        }
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(dto.getStatus().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Estado de orden inválido: " + dto.getStatus() + ". Valores permitidos: CREATED, PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED");
        }
        
        return new OrderCreationData(
            dto.getCustomerId(),
            toDomainAddress(dto.getAddress()),
            dto.getOrderDate(),
            toDomainItems(dto.getItems()),
            status
        );
    }
    
    /**
     * Clase interna para transportar datos de creación de orden
     */
    public static class OrderCreationData {
        private final String customerId;
        private final Address address;
        private final LocalDateTime orderDate;
        private final List<OrderItem> items;
        private final OrderStatus status;
        
        public OrderCreationData(String customerId, Address address, LocalDateTime orderDate, List<OrderItem> items, OrderStatus status) {
            this.customerId = customerId;
            this.address = address;
            this.orderDate = orderDate;
            this.items = items;
            this.status = status;
        }
        
        public String getCustomerId() { return customerId; }
        public Address getAddress() { return address; }
        public LocalDateTime getOrderDate() { return orderDate; }
        public List<OrderItem> getItems() { return items; }
        public OrderStatus getStatus() { return status; }
    }
    
    private List<OrderItem> toDomainItems(List<OrderItemDto> itemDtos) {
        if (itemDtos == null) {
            return null;
        }
        return itemDtos.stream()
                .map(this::toDomainItem)
                .collect(Collectors.toList());
    }
    
    private OrderItem toDomainItem(OrderItemDto dto) {
        if (dto == null) {
            return null;
        }
        return new OrderItem(
            new ProductNumber(dto.getProductNumber()),
            new Quantity(dto.getQuantity()),
            dto.getUnitPrice()
        );
    }

    private Address toDomainAddress(AddressDto dto) {
        if (dto == null) return null;
        return new Address(dto.getStreet(), dto.getCity(), dto.getPostalCode(), dto.getCountry());
    }
} 