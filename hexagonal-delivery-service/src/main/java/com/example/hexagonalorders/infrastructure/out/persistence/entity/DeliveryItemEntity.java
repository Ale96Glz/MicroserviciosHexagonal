package com.example.hexagonalorders.infrastructure.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery_items")
public class DeliveryItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_number", nullable = false)
    private String productNumber;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", referencedColumnName = "delivery_id")
    private DeliveryEntity delivery;

    public DeliveryItemEntity() {}

    public DeliveryItemEntity(String productNumber, int quantity, DeliveryEntity delivery) {
        this.productNumber = productNumber;
        this.quantity = quantity;
        this.delivery = delivery;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductNumber() { return productNumber; }
    public void setProductNumber(String productNumber) { this.productNumber = productNumber; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public DeliveryEntity getDelivery() { return delivery; }
    public void setDelivery(DeliveryEntity delivery) { this.delivery = delivery; }
} 