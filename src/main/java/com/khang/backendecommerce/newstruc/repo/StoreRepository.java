package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity,String> {
    boolean existsByOwner_Id(String userId);
    Optional<StoreEntity> findByOwner_Id(String userId);
}
