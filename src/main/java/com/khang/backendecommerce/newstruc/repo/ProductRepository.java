package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,String> {
}
