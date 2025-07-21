package com.example.hexagonalorders.application.service;

import com.example.hexagonalorders.domain.model.Delivery;
import com.example.hexagonalorders.domain.model.DeliveryStatus;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryId;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryDate;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryAddress;
import com.example.hexagonalorders.domain.port.in.DeliveryUseCase;
import com.example.hexagonalorders.domain.port.out.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de aplicación que implementa los casos de uso de entrega.
 * Este servicio orquesta la lógica de negocio y coordina entre
 * el modelo de dominio y la capa de infraestructura.
 */
@Service
public class DeliveryService implements DeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public Delivery crearEntrega(Delivery delivery) {
        throw new UnsupportedOperationException("Usa crearEntrega(DeliveryCreationData) para crear entregas correctamente.");
    }

    public Delivery crearEntrega(com.example.hexagonalorders.infrastructure.in.web.mapper.DeliveryMapper.DeliveryCreationData data) {
        DeliveryId id = new DeliveryId(UUID.randomUUID().toString());
        Delivery delivery = new Delivery(
            id,
            data.getOrderNumber(),
            data.getDeliveryAddress(),
            data.getScheduledDate(),
            data.getStatus(),
            data.getDeliveryNotes()
        );
        Delivery savedDelivery = deliveryRepository.save(delivery);
        savedDelivery.getDomainEvents().forEach(event -> {
            System.out.println("Domain event published: " + event.getClass().getSimpleName());
        });
        return savedDelivery;
    }

    @Override
    public Delivery programarEntrega(DeliveryId deliveryId, DeliveryDate scheduledDate) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada: " + deliveryId.value()));
        delivery.scheduleDelivery(scheduledDate);
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        updatedDelivery.getDomainEvents().forEach(event -> {
            System.out.println("Domain event published: " + event.getClass().getSimpleName());
        });
        return updatedDelivery;
    }

    @Override
    public Delivery confirmarEntrega(DeliveryId deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada: " + deliveryId.value()));
        delivery.confirmDelivery();
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        updatedDelivery.getDomainEvents().forEach(event -> {
            System.out.println("Domain event published: " + event.getClass().getSimpleName());
        });
        return updatedDelivery;
    }

    @Override
    public Delivery iniciarEntrega(DeliveryId deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada: " + deliveryId.value()));
        delivery.startDelivery();
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        updatedDelivery.getDomainEvents().forEach(event -> {
            System.out.println("Domain event published: " + event.getClass().getSimpleName());
        });
        return updatedDelivery;
    }

    @Override
    public Delivery completarEntrega(DeliveryId deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada: " + deliveryId.value()));
        delivery.completeDelivery();
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        updatedDelivery.getDomainEvents().forEach(event -> {
            System.out.println("Domain event published: " + event.getClass().getSimpleName());
        });
        return updatedDelivery;
    }

    @Override
    public Delivery cancelarEntrega(DeliveryId deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada: " + deliveryId.value()));
        delivery.cancelDelivery();
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        updatedDelivery.getDomainEvents().forEach(event -> {
            System.out.println("Domain event published: " + event.getClass().getSimpleName());
        });
        return updatedDelivery;
    }

    public void eliminarEntrega(com.example.hexagonalorders.domain.model.valueobject.DeliveryId deliveryId) {
        deliveryRepository.deleteById(deliveryId);
    }

    @Override
    public Optional<Delivery> obtenerEntrega(DeliveryId deliveryId) {
        return deliveryRepository.findById(deliveryId);
    }

    @Override
    public List<Delivery> obtenerTodasLasEntregas() {
        return deliveryRepository.findAll();
    }

    @Override
    public List<Delivery> obtenerEntregasPorEstado(String status) {
        try {
            DeliveryStatus deliveryStatus = DeliveryStatus.valueOf(status.toUpperCase());
            return deliveryRepository.findByStatus(deliveryStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de entrega inválido: " + status);
        }
    }
    
    /**
     * Crea una entrega a partir de un evento de confirmación de orden.
     * Este método es llamado por el consumidor de Kafka cuando una orden es confirmada.
     *
     * @param orderId el identificador de la orden
     * @param customerAddress la dirección de entrega del cliente
     * @param items los ítems de la orden (para uso futuro en planificación de entrega)
     */
    public Delivery createDeliveryFromOrder(String orderId, String customerAddress, 
                                          List<com.example.hexagonalorders.infrastructure.in.messaging.OrderEventConsumer.OrderItem> items) {
        DeliveryId deliveryId = new DeliveryId(UUID.randomUUID().toString());
        
        // Analizar dirección del cliente (asumiendo que es una cadena simple por ahora)
        // En un escenario real, podrías querer analizar esto en componentes de dirección estructurados
        DeliveryAddress address = new DeliveryAddress(
            customerAddress, // calle
            "Ciudad Desconocida",  // ciudad - podrías extraer esto de customerAddress
            "Estado Desconocido", // estado
            "00000",         // código postal
            "País Desconocido" // país
        );
        
        DeliveryDate scheduledDate = new DeliveryDate(LocalDateTime.now().plusDays(1)); // Por defecto: día siguiente
        
        Delivery delivery = new Delivery(
            deliveryId,
            orderId,
            address,
            scheduledDate,
            DeliveryStatus.CREATED,
            "Entrega creada desde orden confirmada: " + orderId
        );
        
        Delivery savedDelivery = deliveryRepository.save(delivery);
        
        // Registrar eventos de dominio para depuración
        savedDelivery.getDomainEvents().forEach(event -> {
            System.out.println("Evento de dominio publicado: " + event.getClass().getSimpleName());
        });
        
        return savedDelivery;
    }
} 