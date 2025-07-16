package com.example.hexagonalorders.infrastructure.in.web;

import com.example.hexagonalorders.domain.model.Delivery;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryId;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryDate;
import com.example.hexagonalorders.domain.port.in.DeliveryUseCase;
import com.example.hexagonalorders.infrastructure.in.web.dto.DeliveryDto;
import com.example.hexagonalorders.infrastructure.in.web.dto.DeliveryStatusUpdateRequest;
import com.example.hexagonalorders.infrastructure.in.web.mapper.DeliveryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para operaciones de entrega.
 * Este es un adaptador de entrada en la capa de infraestructura que maneja las solicitudes HTTP
 * y delega al núcleo de la aplicación a través del puerto DeliveryUseCase.
 * 
 * Los endpoints siguen los principios REST representando el recurso "entregas"
 * y usando métodos HTTP apropiados para las operaciones.
 */
@RestController
@RequestMapping("/api/entregas")
@Tag(name = "Entregas", description = "API de gestión de entregas")
public class DeliveryController {

    private final DeliveryUseCase deliveryUseCase;
    private final DeliveryMapper deliveryMapper;

    public DeliveryController(DeliveryUseCase deliveryUseCase, DeliveryMapper deliveryMapper) {
        this.deliveryUseCase = deliveryUseCase;
        this.deliveryMapper = deliveryMapper;
    }

    @Operation(summary = "Crear una nueva entrega", 
               description = "Crea una nueva entrega para una orden específica.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Entrega creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<DeliveryDto> crearEntrega(@RequestBody DeliveryDto deliveryDto) {
        var creationData = deliveryMapper.toCreationData(deliveryDto);
        Delivery savedDelivery = deliveryUseCase instanceof com.example.hexagonalorders.application.service.DeliveryService
            ? ((com.example.hexagonalorders.application.service.DeliveryService)deliveryUseCase).crearEntrega(creationData)
            : null;
        return ResponseEntity.status(201).body(deliveryMapper.toDto(savedDelivery));
    }

    @Operation(summary = "Actualizar estado de entrega", 
               description = "Actualiza el estado de una entrega (programar, confirmar, iniciar, completar, cancelar).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado de entrega actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Entrega no encontrada"),
        @ApiResponse(responseCode = "400", description = "Estado inválido o fecha inválida")
    })
    @PatchMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDto> actualizarEstadoEntrega(
            @PathVariable String deliveryId,
            @RequestBody DeliveryStatusUpdateRequest statusUpdate) {
        
        Delivery updatedDelivery;
        
        switch (statusUpdate.getAction()) {
            case "SCHEDULE":
                updatedDelivery = deliveryUseCase.programarEntrega(
                    new DeliveryId(deliveryId), 
                    new DeliveryDate(statusUpdate.getScheduledDate())
                );
                break;
            case "CONFIRM":
                updatedDelivery = deliveryUseCase.confirmarEntrega(new DeliveryId(deliveryId));
                break;
            case "START":
                updatedDelivery = deliveryUseCase.iniciarEntrega(new DeliveryId(deliveryId));
                break;
            case "COMPLETE":
                updatedDelivery = deliveryUseCase.completarEntrega(new DeliveryId(deliveryId));
                break;
            case "CANCEL":
                updatedDelivery = deliveryUseCase.cancelarEntrega(new DeliveryId(deliveryId));
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(deliveryMapper.toDto(updatedDelivery));
    }

    @Operation(summary = "Obtener detalles de una entrega", 
               description = "Obtiene los detalles completos de una entrega específica.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Entrega encontrada"),
        @ApiResponse(responseCode = "404", description = "Entrega no encontrada")
    })
    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDto> obtenerEntrega(@PathVariable String deliveryId) {
        return deliveryUseCase.obtenerEntrega(new DeliveryId(deliveryId))
                .map(deliveryMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener todas las entregas", 
               description = "Obtiene una lista de todas las entregas en el sistema.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de entregas obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<DeliveryDto>> obtenerTodasLasEntregas() {
        List<DeliveryDto> deliveries = deliveryUseCase.obtenerTodasLasEntregas()
                .stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(deliveries);
    }

    @Operation(summary = "Obtener entregas por estado", 
               description = "Obtiene una lista de entregas filtradas por estado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de entregas obtenida exitosamente")
    })
    @GetMapping("/estado/{status}")
    public ResponseEntity<List<DeliveryDto>> obtenerEntregasPorEstado(@PathVariable String status) {
        List<DeliveryDto> deliveries = deliveryUseCase.obtenerEntregasPorEstado(status)
                .stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(deliveries);
    }

    @Operation(summary = "Eliminar una entrega", description = "Elimina una entrega por su identificador.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Entrega eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Entrega no encontrada")
    })
    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Void> eliminarEntrega(@PathVariable String deliveryId) {
        DeliveryId id = new DeliveryId(deliveryId);
        // Intentar obtener la entrega antes de eliminar
        if (deliveryUseCase.obtenerEntrega(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Eliminar (deberás implementar el método en el caso de uso y servicio)
        if (deliveryUseCase instanceof com.example.hexagonalorders.application.service.DeliveryService) {
            ((com.example.hexagonalorders.application.service.DeliveryService)deliveryUseCase).eliminarEntrega(id);
        }
        return ResponseEntity.noContent().build();
    }
} 