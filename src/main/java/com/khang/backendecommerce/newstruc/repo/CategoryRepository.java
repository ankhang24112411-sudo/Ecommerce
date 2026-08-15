package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.CategoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    @Query("""
            select c
            from OrderItem oi
            join oi.product p
            join p.category c
            group by c.id
            order by sum(oi.unitPrice*oi.quantity) DESC
            
            """)
    List<CategoryEntity> getFeaturedCategory(Pageable pageable);
}
