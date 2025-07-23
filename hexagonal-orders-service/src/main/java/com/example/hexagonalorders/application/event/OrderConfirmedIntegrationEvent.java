package com.example.hexagonalorders.application.event;

import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
* Integration event for order confirmation.
* This event is used for external communication and should have a stable contract.
*/
public class OrderConfirmedIntegrationEvent {
   private final String orderNumber;
   private final String eventType;
  
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
   private final LocalDateTime confirmedAt;

   private final String street;
   private final String city;
   private final String postalCode;
   private final String country;
   private final List<ItemDto> items;

   public OrderConfirmedIntegrationEvent(OrderNumber orderNumber, String street, String city, String postalCode, String country, List<ItemDto> items) {
       this.orderNumber = orderNumber.value();
       this.eventType = "OrderConfirmed";
       this.confirmedAt = LocalDateTime.now();
       this.street = street;
       this.city = city;
       this.postalCode = postalCode;
       this.country = country;
       this.items = items;
   }

   public String getOrderNumber() {
       return orderNumber;
   }

   public String getEventType() {
       return eventType;
   }

   public LocalDateTime getConfirmedAt() {
       return confirmedAt;
   }

   public String getStreet() { return street; }
   public String getCity() { return city; }
   public String getPostalCode() { return postalCode; }
   public String getCountry() { return country; }
   public List<ItemDto> getItems() { return items; }

   public static class ItemDto {
       private final String productNumber;
       private final int quantity;
       private final double unitPrice;
       public ItemDto(String productNumber, int quantity, double unitPrice) {
           this.productNumber = productNumber;
           this.quantity = quantity;
           this.unitPrice = unitPrice;
       }
       public String getProductNumber() { return productNumber; }
       public int getQuantity() { return quantity; }
       public double getUnitPrice() { return unitPrice; }
   }
} 