package com.khang.backendecommerce.domain.discount.repo;

import com.khang.backendecommerce.domain.discount.entity.DiscountCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountCartRepository extends JpaRepository<DiscountCartEntity,String> {
}
