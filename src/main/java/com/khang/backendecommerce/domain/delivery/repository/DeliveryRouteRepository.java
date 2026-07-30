package com.khang.backendecommerce.domain.delivery.repository;

import com.khang.backendecommerce.domain.delivery.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.domain.delivery.entity.DeliveryRouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRouteRepository extends JpaRepository<DeliveryRouteEntity,String> {
    Optional<DeliveryRouteEntity> findByStateFrom_IdAndStateTo_Id(String stateFromId, String stateToId);

    Optional<DeliveryFeeEntity> findByDeliveryRouteId(String id);
}
