package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubOrderRepository extends JpaRepository<SubOrderEntity , String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph (
            attributePaths = {
                    "order" , "orderItems.inventory"
            }
    )
    @Query("""
    SELECT so
    FROM SubOrder so
    JOIN so.store s
    JOIN s.owner o
    WHERE o.id = :sellerId
      AND so.status = 'PENDING'
""")
    Page<SubOrderEntity> findPendingBySellerId(@Param("sellerId") String sellerId, Pageable pageable);

    boolean existsByIdAndStore_Owner_Id(String subOrderId, String sellerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph (
            attributePaths = {
                    "order" , "orderItems.inventory"
            }
    )
        @Query("""
    SELECT so
    FROM SubOrder so
    JOIN so.store s
    JOIN s.owner o
    WHERE o.id = :sellerId
      AND so.id = :subOrderId
""")
        SubOrderEntity findSubOrder(@Param("sellerId") String sellerId, @Param("orderId") String subOrderId);


}
