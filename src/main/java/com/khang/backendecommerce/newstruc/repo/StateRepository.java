package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<StateEntity, String> {
}
