package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String>, JpaSpecificationExecutor<ProductEntity> {
    @EntityGraph(
            attributePaths = {"store"},
            type = EntityGraph.EntityGraphType.FETCH
    )
    @Query("""
            select product
            from ProductEntity product
            where product.id =:productId
            """)
    Optional<ProductEntity> findProductAndShopByProductId(@Param("productId") String productId);


    @Query("""
            select p.id
            from OrderItem i
            join i.product p
            group by p.id
            order by sum(i.quantity) desc
            """)
    List<String> getFeaturedProduct(Pageable pageable);
//
//    @Query("""
//select p
//from OrderEntity oe
//join oe.subOrders so
//join so.orderItems oi
//join oi.product p
//join fetch p.inventoryList i
//where oe.id =:orderId
//""")
//    List<ProductEntity> getAllProductByOrderId(@Param("orderId") String orderId);
}


