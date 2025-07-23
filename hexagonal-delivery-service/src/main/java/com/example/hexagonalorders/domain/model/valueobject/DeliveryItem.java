package com.example.hexagonalorders.domain.model.valueobject;

import java.util.Objects;

public class DeliveryItem {
    private final ProductNumber productNumber;
    private final Quantity quantity;

    public DeliveryItem(ProductNumber productNumber, Quantity quantity) {
        if (productNumber == null) {
            throw new IllegalArgumentException("Product number cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

    public ProductNumber getProductNumber() {
        return productNumber;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryItem that = (DeliveryItem) o;
        return Objects.equals(productNumber, that.productNumber) &&
               Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productNumber, quantity);
    }

    @Override
    public String toString() {
        return "DeliveryItem{" +
                "productNumber=" + productNumber +
                ", quantity=" + quantity +
                '}';
    }
} 