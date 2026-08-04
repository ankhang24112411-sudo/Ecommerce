package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.SubOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubOrderRepository extends JpaRepository<SubOrderEntity , String> {
}
