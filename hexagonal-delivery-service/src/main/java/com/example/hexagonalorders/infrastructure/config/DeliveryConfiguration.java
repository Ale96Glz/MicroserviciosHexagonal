package com.example.hexagonalorders.infrastructure.config;

import com.example.hexagonalorders.application.service.DeliveryService;
import com.example.hexagonalorders.domain.port.in.DeliveryUseCase;
import com.example.hexagonalorders.domain.port.out.DeliveryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for delivery management components.
 * This class sets up the dependency injection for the delivery domain.
 */
@Configuration
public class DeliveryConfiguration {
    
    /**
     * Configuración de la inyección de dependencias para el caso de uso de entrega.
     */
    @Bean
    public DeliveryUseCase deliveryUseCase(DeliveryRepository deliveryRepository) {
        return new DeliveryService(deliveryRepository);
    }
    

} 