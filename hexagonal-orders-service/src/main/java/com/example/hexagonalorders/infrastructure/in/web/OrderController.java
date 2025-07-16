package com.example.hexagonalorders.infrastructure.in.web;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.port.in.OrderUseCase;
import com.example.hexagonalorders.infrastructure.in.web.dto.OrderDto;
import com.example.hexagonalorders.infrastructure.in.web.dto.OrderResponseDto;
import com.example.hexagonalorders.infrastructure.in.web.mapper.OrderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for order operations.
 * This is an input adapter in the infrastructure layer that handles HTTP requests
 * and delegates to the application core through the OrderUseCase port.
 */
@RestController
@RequestMapping("/api/orders")
// @RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management API")
public class OrderController {

    private final OrderUseCase orderUseCase;
    private final OrderMapper orderMapper;

    public OrderController(OrderUseCase orderUseCase, OrderMapper orderMapper) {
        this.orderUseCase = orderUseCase;
        this.orderMapper = orderMapper;
    }

    @Operation(summary = "Create a new order", description = "Creates a new order and returns the created order.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderDto orderDto) {
        OrderMapper.OrderCreationData orderData = orderMapper.createOrderData(orderDto);
        
        // Usar el método que devuelve el id
        var orderWithId = ((com.example.hexagonalorders.application.service.OrderService) orderUseCase).createOrderWithId(orderData);
        
        return ResponseEntity.ok(orderMapper.toResponseDto(orderWithId.getOrder(), orderWithId.getId()));
    }

    @Operation(summary = "Get an order by order number", description = "Retrieves an order by its order number.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order found"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable String orderNumber) {
        // Usar el método que devuelve el id
        var orderWithId = ((com.example.hexagonalorders.application.service.OrderService) orderUseCase)
            .getOrderWithId(new OrderNumber(orderNumber));
        
        if (orderWithId.isPresent()) {
            return ResponseEntity.ok(orderMapper.toResponseDto(orderWithId.get().getOrder(), orderWithId.get().getId()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete an order by order number", description = "Deletes an order by its order number.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Order deleted successfully")
    })
    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderNumber) {
        orderUseCase.deleteOrder(new OrderNumber(orderNumber));
        return ResponseEntity.noContent().build();
    }
} 