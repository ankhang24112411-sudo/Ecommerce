package com.khang.backendecommerce.domain.delivery.repository;

import com.khang.backendecommerce.domain.delivery.entity.DeliveryFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryFeeRepository extends JpaRepository<DeliveryFeeEntity,String> {
}
