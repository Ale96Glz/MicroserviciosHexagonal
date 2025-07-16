package com.example.hexagonalorders.domain.port.in;

import com.example.hexagonalorders.domain.model.Delivery;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryId;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryDate;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada que define el contrato para las operaciones de entrega.
 * Esta interfaz representa las operaciones principales del negocio que se pueden realizar
 * sobre las entregas, usando el lenguaje ubicuo del dominio de entregas.
 */
public interface DeliveryUseCase {
    
    /**
     * Crea una nueva entrega para una orden.
     * Esta acción representa la acción de negocio de crear una entrega.
     * 
     * @param delivery la entrega a crear
     * @return la entrega creada
     */
    Delivery crearEntrega(Delivery delivery);

    /**
     * Programar una entrega para una fecha y hora específicas.
     * Esta acción representa la acción de negocio de programar una entrega.
     * 
     * @param deliveryId el identificador de la entrega
     * @param scheduledDate la nueva fecha programada
     * @return la entrega actualizada
     */
    Delivery programarEntrega(DeliveryId deliveryId, DeliveryDate scheduledDate);

    /**
     * Confirma que una entrega está lista para recoger.
     * Esta acción representa la acción de negocio de confirmar la preparación de la entrega.
     * 
     * @param deliveryId el identificador de la entrega
     * @return la entrega actualizada
     */
    Delivery confirmarEntrega(DeliveryId deliveryId);

    /**
     * Inicia el proceso de entrega (marca como en tránsito).
     * Esta acción representa la acción de negocio de iniciar la entrega.
     * 
     * @param deliveryId el identificador de la entrega
     * @return la entrega actualizada
     */
    Delivery iniciarEntrega(DeliveryId deliveryId);

    /**
     * Completa el proceso de entrega.
     * Esta acción representa la acción de negocio de completar una entrega.
     * 
     * @param deliveryId el identificador de la entrega
     * @return la entrega actualizada
     */
    Delivery completarEntrega(DeliveryId deliveryId);

    /**
     * Cancela una entrega.
     * Esta acción representa la acción de negocio de cancelar una entrega.
     * 
     * @param deliveryId el identificador de la entrega
     * @return la entrega actualizada
     */
    Delivery cancelarEntrega(DeliveryId deliveryId);

    /**
     * Recupera una entrega por su identificador.
     * 
     * @param deliveryId el identificador de la entrega
     * @return la entrega si se encuentra
     */
    Optional<Delivery> obtenerEntrega(DeliveryId deliveryId);

    /**
     * Recupera todas las entregas.
     * 
     * @return lista de todas las entregas
     */
    List<Delivery> obtenerTodasLasEntregas();

    /**
     * Recupera entregas por estado.
     * 
     * @param status el estado de la entrega por el que filtrar
     * @return lista de entregas con el estado especificado
     */
    List<Delivery> obtenerEntregasPorEstado(String status);
} 