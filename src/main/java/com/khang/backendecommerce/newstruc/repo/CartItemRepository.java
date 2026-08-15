package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.CartItemEntity;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, String> {


    Optional<CartItemEntity> findByIdAndCart_User_Id(String cartItemId, String userId);

    boolean existsByIdAndCart_Id(String cartItemId, String cartId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "product",
                    "product.store"
            }
    )
    @Query("""
            select cartItem
            from CartItemEntity cartItem
            where cartItem.cart.id = :cartId
            order by cartItem.id
            """)
    List<CartItemEntity> findAllForCheckout(@Param("cartId") String cartId);

    @Modifying(flushAutomatically = true)
    @Query("""
            delete from CartItemEntity cartItem
            where cartItem.cart.id = :cartId
            """)
    int deletePurchasedItems(@Param("cartId") String cartId);

    void deleteAllByCart_Id(String cartId);

    @Query("""
            select cartItem
            from CartItemEntity cartItem
            where cartItem.cart.id = :cartId
            order by cartItem.id
            """)
    List<CartItemEntity> findAllCartItem(@Param("cartId") String cartId);

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "product"
            }
    )
    @Query("""
            select cartItem
            from CartItemEntity cartItem
            where cartItem.cart.id = :cartId
            order by cartItem.id
            """)
    List<CartItemEntity> findAllCartItemInGetAllWithProduct(@Param("cartId") String cartId);
}
