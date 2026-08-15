package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.BannerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, String> {
    @Query("""
            select b
            from BannerEntity b
            where b.isActive is true
            order by displayOrder asc
            """)
    List<BannerEntity> getBanner(Pageable pageable);
}
