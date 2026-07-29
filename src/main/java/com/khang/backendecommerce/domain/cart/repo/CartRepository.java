package com.khang.backendecommerce.domain.cart.repo;

import com.khang.backendecommerce.domain.cart.dto.response.CartItemResponse;
import com.khang.backendecommerce.domain.cart.entity.CartEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, String> {

    @Query(value = """

            select p.image, p.name , tci.subtotal , 
                   cast(tci.inventory_status as VARCHAR)
from tbl_cart tc\s
join tbl_cart_item tci\s
on tc.id = tci.cart_id\s
join tbl_product p
on tci.product_id = p.id
where tc.user_id = 's'
""" , nativeQuery = true)
    List<CartItemResponse> getAllCartItems(@Param("userId") String userId);
}
