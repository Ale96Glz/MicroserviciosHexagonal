package com.example.hexagonalorders.infrastructure.in.web.dto;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OrderItem;
import com.example.hexagonalorders.infrastructure.in.web.dto.AddressDto;
import com.example.hexagonalorders.domain.model.valueobject.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object for Order response data.
 * This class is used specifically for API responses and includes the database ID.
 */
@Data
public class OrderResponseDto {
    @Schema(description = "Technical identifier of the order from database.", example = "123", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Business order number.", example = "ORD-20240618-001", accessMode = Schema.AccessMode.READ_ONLY)
    private String orderNumber;

    @Schema(description = "Customer identifier.", example = "CUST-001")
    private String customerId;

    @Schema(description = "Order creation date.", example = "2024-06-18T10:00:00")
    private LocalDateTime orderDate;

    @Schema(description = "List of order items.")
    private List<OrderItemResponseDto> items;

    @Schema(description = "Order status.", example = "CREATED")
    private String status;

    private AddressDto address;

    public OrderResponseDto() {}

    public OrderResponseDto(Order order, Long id) {
        this.id = id;
        this.orderNumber = order.getOrderNumber().value();
        this.customerId = order.getCustomerId();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus().name();
        this.items = order.getItems().stream()
                .map(OrderItemResponseDto::new)
                .collect(Collectors.toList());
        Address addr = order.getAddress();
        if (addr != null) {
            this.address = new AddressDto(addr.street(), addr.city(), addr.postalCode(), addr.country());
        }
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public List<OrderItemResponseDto> getItems() { return items; }
    public void setItems(List<OrderItemResponseDto> items) { this.items = items; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public AddressDto getAddress() { return address; }
    public void setAddress(AddressDto address) { this.address = address; }
} 