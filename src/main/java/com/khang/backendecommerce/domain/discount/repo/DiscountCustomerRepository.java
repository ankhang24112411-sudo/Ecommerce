package com.khang.backendecommerce.domain.discount.repo;

import com.khang.backendecommerce.domain.discount.entity.DiscountCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountCustomerRepository extends JpaRepository<DiscountCustomerEntity, String> {
    Optional<DiscountCustomerEntity> findByCustomer_IdAndDiscount_DiscountName(String userId, String discountName);
}
