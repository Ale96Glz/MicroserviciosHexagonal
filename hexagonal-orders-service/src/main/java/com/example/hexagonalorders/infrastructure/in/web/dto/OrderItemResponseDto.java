package com.example.hexagonalorders.infrastructure.in.web.dto;

import com.example.hexagonalorders.domain.model.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * Data Transfer Object for OrderItem response data.
 */
public class OrderItemResponseDto {
    @Schema(description = "Technical identifier of the order item from database.", example = "456", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Product number.", example = "PROD-001")
    private String productNumber;

    @Schema(description = "Quantity ordered.", example = "2")
    private Integer quantity;

    @Schema(description = "Unit price.", example = "99.99")
    private BigDecimal unitPrice;

    public OrderItemResponseDto() {}

    public OrderItemResponseDto(OrderItem item) {
        this.productNumber = item.getProductNumber().value();
        this.quantity = item.getQuantity().value();
        this.unitPrice = item.getUnitPrice();
    }

    public OrderItemResponseDto(OrderItem item, Long id) {
        this.id = id;
        this.productNumber = item.getProductNumber().value();
        this.quantity = item.getQuantity().value();
        this.unitPrice = item.getUnitPrice();
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getProductNumber() { return productNumber; }
    public void setProductNumber(String productNumber) { this.productNumber = productNumber; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
} 