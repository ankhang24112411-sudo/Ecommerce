package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.dto.response.store.TotalSubOrderDailyDashboard;
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

import java.time.Instant;

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
    FROM SubOrderEntity so
    JOIN so.store s
    JOIN s.owner o
    WHERE o.id = :sellerId
      AND so.orderStatus = 'PENDING'
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
    FROM SubOrderEntity so
    JOIN so.store s
    JOIN s.owner o
    WHERE o.id = :sellerId
      AND so.id = :subOrderId
""")
        SubOrderEntity findSubOrder(@Param("sellerId") String sellerId, @Param("subOrderId") String subOrderId);

    @Query("""
  select
  count(so) as totalSubOrder,
  sum ( case when so.orderStatus = 'CONFIRMED' then 1 else 0 end) as confirmSubOrder,
    sum ( case when so.orderStatus = 'PENDING' then 1 else 0 end) as pendingSubOrder
    from SubOrderEntity so
    JOIN so.store s
    JOIN s.owner o
    WHERE o.id = :sellerId
    AND so.createdAt >= :fromDate
    AND so.createdAt < :toDate
    

           
""")
    TotalSubOrderDailyDashboard getTodayDashboard(String sellerId,  Instant fromDate,Instant toDate);
    @EntityGraph (
            attributePaths = {
                    "order"
            }
    )
    SubOrderEntity findByTrackingCode(String trackingCode);

}
