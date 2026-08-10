package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.RefundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<RefundEntity,String> {

}
