package com.example.hexagonalorders.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO para actualizar el estado de una entrega.
 * Este DTO se utiliza para especificar la acción a realizar sobre una entrega.
 */
public class DeliveryStatusUpdateRequest {
    
    @Schema(description = "Acción a realizar sobre la entrega.", 
            example = "SCHEDULE", 
            allowableValues = {"SCHEDULE", "CONFIRM", "START", "COMPLETE", "CANCEL"}, 
            required = true)
    private String action;
    
    @Schema(description = "Fecha y hora programada para la entrega (solo requerido para SCHEDULE).", 
            example = "2024-07-08T14:00:00")
    private LocalDateTime scheduledDate;

    public DeliveryStatusUpdateRequest() {}

    public DeliveryStatusUpdateRequest(String action, LocalDateTime scheduledDate) {
        this.action = action;
        this.scheduledDate = scheduledDate;
    }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }
} 