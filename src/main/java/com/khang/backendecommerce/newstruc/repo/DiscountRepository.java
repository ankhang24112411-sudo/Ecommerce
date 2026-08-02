package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, String> {
    Optional<DiscountEntity> findByDiscountName(String discountName);
}
