package com.example.hexagonalorders.infrastructure.in.web.dto;

public class DeliveryItemDto {
    private String productNumber;
    private int quantity;

    public DeliveryItemDto() {}

    public DeliveryItemDto(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

    public String getProductNumber() { return productNumber; }
    public void setProductNumber(String productNumber) { this.productNumber = productNumber; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
} 