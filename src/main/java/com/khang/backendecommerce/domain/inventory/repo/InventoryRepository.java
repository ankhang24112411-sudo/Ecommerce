package com.khang.backendecommerce.domain.inventory.repo;

import com.khang.backendecommerce.domain.inventory.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity,String> {

    Optional<InventoryEntity> findByProduct_Id(String productId);
}
