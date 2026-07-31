package com.khang.backendecommerce.domain.cart.repo;

import com.khang.backendecommerce.domain.cart.dto.response.CartItemResponse;
import com.khang.backendecommerce.domain.cart.entity.CartEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, String> {

    @Query(value = """

            select p.image, p.name , tci.subtotal , tci.quantity,
                   cast(tci.inventory_status as VARCHAR)
from tbl_cart tc\s
join tbl_cart_item tci\s
on tc.id = tci.cart_id\s
join tbl_product p
on tci.product_id = p.id
where tc.user_id = :userId
""" , nativeQuery = true)
    List<CartItemResponse> getAllCartItems(@Param("userId") String userId);

    CartEntity findByUser_Id(String id);


//@Query(value = """
//
//        select tbi.image , tp.name , tci.subtotal , tci.quantity , cast(tci.inventory_status as VARCHAR) \s
//from tbl_cart_item tci \s
//join tbl_product tp\s
//on tci.product_id = tp.id\s
//join tbl_product_image tbi
//on tbi.product_id = tp.id\s
//where tbi.is_primary = 1
//and tci.cart_id = :cartItemId
//
//""" , nativeQuery = true)
//List<CartItemResponse> getCartItems(@Param("cartItemId") String cartItemId);

}

