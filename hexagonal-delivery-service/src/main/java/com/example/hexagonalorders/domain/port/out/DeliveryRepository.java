package com.example.hexagonalorders.domain.port.out;

import com.example.hexagonalorders.domain.model.Delivery;
import com.example.hexagonalorders.domain.model.DeliveryStatus;
import com.example.hexagonalorders.domain.model.valueobject.DeliveryId;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida que define el contrato para las operaciones de persistencia de entregas.
 * Esta interfaz es parte de la capa de dominio y define las operaciones de persistencia
 * que se pueden realizar sobre las entregas.
 */
public interface DeliveryRepository {
    
    /**
     * Guarda una entrega en el repositorio.
     * 
     * @param delivery la entrega a guardar
     * @return la entrega guardada
     */
    Delivery save(Delivery delivery);
    
    /**
     * Busca una entrega por su identificador.
     * 
     * @param deliveryId el identificador de la entrega
     * @return la entrega si se encuentra
     */
    Optional<Delivery> findById(DeliveryId deliveryId);
    
    /**
     * Busca todas las entregas.
     * 
     * @return lista de todas las entregas
     */
    List<Delivery> findAll();
    
    /**
     * Busca entregas por estado.
     * 
     * @param status el estado de la entrega por el que filtrar
     * @return lista de entregas con el estado especificado
     */
    List<Delivery> findByStatus(DeliveryStatus status);
    
    /**
     * Elimina una entrega por su identificador.
     * 
     * @param deliveryId el identificador de la entrega
     */
    void deleteById(DeliveryId deliveryId);
} 