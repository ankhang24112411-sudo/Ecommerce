package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {
}
