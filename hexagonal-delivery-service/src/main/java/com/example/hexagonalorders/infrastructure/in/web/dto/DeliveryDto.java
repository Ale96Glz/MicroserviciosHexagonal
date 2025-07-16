package com.example.hexagonalorders.infrastructure.in.web.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO para los datos de la entrega.
 * Esta clase se utiliza para transferir datos de entrega entre la capa adaptadora
 * y la capa de aplicación, usando el lenguaje ubicuo del dominio de entregas.
 */
@Data
public class DeliveryDto {
    @Schema(description = "Identificador único de la entrega. Generado por el backend.", 
            example = "DEL-20240708-001", accessMode = Schema.AccessMode.READ_ONLY)
    private String deliveryId;

    @Schema(description = "Número de orden asociada a la entrega.", 
            example = "ORD-20240708-001", required = true)
    private String orderNumber;

    @Schema(description = "Dirección de entrega.", required = true)
    private DeliveryAddressDto deliveryAddress;

    @Schema(description = "Fecha y hora programada para la entrega.", 
            example = "2024-07-08T14:00:00", required = true)
    private LocalDateTime scheduledDate;

    @Schema(description = "Estado actual de la entrega.", 
            example = "SCHEDULED", required = true)
    private String status;

    @Schema(description = "Notas adicionales sobre la entrega.", 
            example = "Entregar en la puerta principal")
    private String deliveryNotes;

    public DeliveryDto() {}

    public DeliveryDto(String deliveryId, String orderNumber, DeliveryAddressDto deliveryAddress, 
                      LocalDateTime scheduledDate, String status, String deliveryNotes) {
        this.deliveryId = deliveryId;
        this.orderNumber = orderNumber;
        this.deliveryAddress = deliveryAddress;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.deliveryNotes = deliveryNotes;
    }

    // Getters manuales para evitar problemas con Lombok
    public String getDeliveryId() { return deliveryId; }
    public String getOrderNumber() { return orderNumber; }
    public DeliveryAddressDto getDeliveryAddress() { return deliveryAddress; }
    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public String getStatus() { return status; }
    public String getDeliveryNotes() { return deliveryNotes; }

    // Setters manuales para evitar problemas con Lombok
    public void setDeliveryId(String deliveryId) { this.deliveryId = deliveryId; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public void setDeliveryAddress(DeliveryAddressDto deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }
    public void setStatus(String status) { this.status = status; }
    public void setDeliveryNotes(String deliveryNotes) { this.deliveryNotes = deliveryNotes; }
} 