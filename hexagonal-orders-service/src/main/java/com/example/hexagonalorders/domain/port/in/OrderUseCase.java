package com.example.hexagonalorders.domain.port.in;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.infrastructure.in.web.mapper.OrderMapper.OrderCreationData;

import java.util.List;
import java.util.Optional;

/**
 * Input port defining the contract for order operations.
 * This interface is part of the domain layer and defines the core business operations
 * that can be performed on orders.
 */
public interface OrderUseCase {
    /**
     * Creates a new order
     * @param orderData the order data to create (without order number)
     * @return the created order
     */
    Order createOrder(OrderCreationData orderData);

    /**
     * Retrieves an order by its order number
     * @param orderNumber the order number
     * @return the order if found
     */
    Optional<Order> getOrder(OrderNumber orderNumber);

    /**
     * Retrieves all orders
     * @return list of all orders
     */
    List<Order> getAllOrders();

    /**
     * Deletes an order by its order number
     * @param orderNumber the order number
     */
    void deleteOrder(OrderNumber orderNumber);

    /**
     * Confirma una orden por su número de orden
     * @param orderNumber el número de la orden
     */
    void confirmOrder(OrderNumber orderNumber);
} 