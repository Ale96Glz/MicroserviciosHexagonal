package com.example.hexagonalorders.application.service;

import com.example.hexagonalorders.domain.model.Delivery;
import com.example.hexagonalorders.domain.model.DeliveryStatus;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryId;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryDate;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryAddress;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryItem;
import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
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
            data.getDeliveryNotes(),
            new java.util.ArrayList<>()
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
    public Delivery createDeliveryFromOrder(String orderId, String street, String city, String postalCode, String country, 
                                          List<com.example.hexagonalorders.infrastructure.in.messaging.OrderEventConsumer.OrderItem> items) {
        DeliveryId deliveryId = new DeliveryId(UUID.randomUUID().toString());
        DeliveryAddress address = new DeliveryAddress(
            street,
            city,
            "Estado Desconocido",
            postalCode,
            country
        );
        DeliveryDate scheduledDate = new DeliveryDate(LocalDateTime.now().plusDays(1));
        // Mapear los items del evento a DeliveryItem
        List<DeliveryItem> deliveryItems = new java.util.ArrayList<>();
        if (items != null) {
            for (com.example.hexagonalorders.infrastructure.in.messaging.OrderEventConsumer.OrderItem item : items) {
                deliveryItems.add(new DeliveryItem(
                    new ProductNumber(item.getProductNumber()),
                    new Quantity(item.getQuantity())
                ));
            }
        }
        Delivery delivery = new Delivery(
            deliveryId,
            orderId,
            address,
            scheduledDate,
            DeliveryStatus.CREATED,
            "Entrega creada desde orden confirmada: " + orderId,
            deliveryItems
        );
        Delivery savedDelivery = deliveryRepository.save(delivery);
        savedDelivery.getDomainEvents().forEach(event -> {
            System.out.println("Evento de dominio publicado: " + event.getClass().getSimpleName());
        });
        return savedDelivery;
    }
} 