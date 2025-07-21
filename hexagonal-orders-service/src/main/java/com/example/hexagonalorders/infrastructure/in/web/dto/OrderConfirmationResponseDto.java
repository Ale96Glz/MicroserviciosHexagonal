package com.example.hexagonalorders.infrastructure.in.web.dto;

/**
 * Data Transfer Object for Order confirmation response.
 * Contains only the essential information after confirming an order.
 */
public class OrderConfirmationResponseDto {
    private Long id;
    private String orderNumber;
    private String status;

    public OrderConfirmationResponseDto() {}

    public OrderConfirmationResponseDto(Long id, String orderNumber, String status) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.status = status;
    }

    // Getters
    public Long getId() { return id; }
    public String getOrderNumber() { return orderNumber; }
    public String getStatus() { return status; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public void setStatus(String status) { this.status = status; }
} 