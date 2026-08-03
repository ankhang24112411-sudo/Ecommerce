package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.InventoryEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity,String> {

    Optional<InventoryEntity> findByProduct_Id(String productId);

    List<InventoryEntity> findAllByProduct_IdIn(Collection<String> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select inventory
    from InventoryEntity inventory
    where inventory.product.id in :productIds
""")
    List<InventoryEntity> findAllByProductIdsForUpdate(@Param("productIds") Collection<String> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(
            attributePaths = {
                    "product.store",
                    "warehouse.state"
            }
    )
    @Query("""
   select inventory
   from InventoryEntity inventory
   where inventory.product.id in :productIds
""")
   List<InventoryEntity> findAllInventoryCandidates(@Param("productIds") Collection<String> productIds);
}
