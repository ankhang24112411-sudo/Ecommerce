package com.khang.backendecommerce.domain.inventory.repo;

import com.khang.backendecommerce.domain.inventory.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity,String> {

    Optional<InventoryEntity> findByProduct_Id(String productId);

    List<InventoryEntity> findAllByProduct_IdIn(Collection<String> productIds);
}
