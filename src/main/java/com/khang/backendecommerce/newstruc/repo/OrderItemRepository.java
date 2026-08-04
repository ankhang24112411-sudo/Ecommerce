package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,String> {
}
