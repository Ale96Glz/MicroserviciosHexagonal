package com.example.hexagonalorders.infrastructure.config;

import com.example.hexagonalorders.domain.service.OrderValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for order-related beans.
 * This class is part of the infrastructure layer and is responsible for:
 * - Wiring together domain, application, and adapter components
 * - Managing infrastructure concerns
 * - Providing Spring beans for dependency injection
 */
@Configuration
public class OrderConfiguration {

    @Bean
    public OrderValidationService orderValidationService() {
        return new OrderValidationService();
    }
} 