package com.example.hexagonalorders.infrastructure.out.persistence.repository;

import com.example.hexagonalorders.infrastructure.out.persistence.entity.DeliveryEntity;
import com.example.hexagonalorders.domain.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryJpaRepository extends JpaRepository<DeliveryEntity, Long> {
    
    Optional<DeliveryEntity> findByDeliveryId(String deliveryId);
    
    List<DeliveryEntity> findByRouteId(String routeId);
    
    List<DeliveryEntity> findByDeliveryPersonId(String deliveryPersonId);
    
    List<DeliveryEntity> findByOrderNumber(String orderNumber);
    
    List<DeliveryEntity> findByStatus(DeliveryStatus status);
    
    List<DeliveryEntity> findByScheduledDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT d FROM DeliveryEntity d WHERE d.scheduledDate < :now AND d.status NOT IN ('DELIVERED', 'CANCELLED')")
    List<DeliveryEntity> findOverdueDeliveries(@Param("now") LocalDateTime now);
    
    boolean existsByDeliveryId(String deliveryId);
    
    long countByStatus(DeliveryStatus status);
} 