package com.example.hexagonalorders.domain.model;

import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import java.math.BigDecimal;

public class OrderItem {
    private final ProductNumber productNumber;
    private final Quantity quantity;
    private final BigDecimal unitPrice;
    private final Long id;

    public OrderItem(ProductNumber productNumber, Quantity quantity, BigDecimal unitPrice) {
        this(productNumber, quantity, unitPrice, null);
    }

    public OrderItem(ProductNumber productNumber, Quantity quantity, BigDecimal unitPrice, Long id) {
        if (productNumber == null) {
            throw new IllegalArgumentException("Product number cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than zero");
        }
        this.productNumber = productNumber;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.id = id;
    }

    public ProductNumber getProductNumber() {
        return productNumber;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Long getId() {
        return id;
    }
} 