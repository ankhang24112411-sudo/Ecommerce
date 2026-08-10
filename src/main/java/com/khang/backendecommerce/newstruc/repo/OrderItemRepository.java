package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.OrderItem;
import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,String> {
    @EntityGraph(
            attributePaths = {
                    "product" , "inventory"
            }, type = EntityGraph.EntityGraphType.FETCH
    )
    @Query("""
select oi
from OrderEntity oe
join oe.subOrders so
join  so.orderItems oi
where oe.id =:orderId
""")
    List<OrderItem> getAllOrderItemsByOrderId(@Param("orderId") String orderId);
}
