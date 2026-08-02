package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.DeliveryFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryFeeRepository extends JpaRepository<DeliveryFeeEntity,String> {
    Optional<DeliveryFeeEntity> findByDeliveryRoute_Id(String id);

}
