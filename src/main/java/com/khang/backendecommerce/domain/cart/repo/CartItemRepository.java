package com.khang.backendecommerce.domain.cart.repo;

import com.khang.backendecommerce.domain.cart.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity,String> {
    Optional<CartItemEntity> findByIdAndCart_User_Id(String cartItemId , String userId);

}
