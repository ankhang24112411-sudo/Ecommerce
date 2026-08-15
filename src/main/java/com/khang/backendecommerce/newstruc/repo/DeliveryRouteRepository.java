package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.DeliveryRouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRouteRepository extends JpaRepository<DeliveryRouteEntity, String> {
    Optional<DeliveryRouteEntity> findByStateFrom_IdAndStateTo_Id(String stateFromId, String stateToId);

}
