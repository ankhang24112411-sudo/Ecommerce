package com.khang.backendecommerce.newstruc.repo;

import com.khang.backendecommerce.newstruc.entity.ProductImageEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageEntity,String> {
    @Query("""
select pi
from ProductImageEntity pi
join pi.product p
where p.id in (:Ids)
and pi.primary = 1
""")
    List<ProductImageEntity> getFeaturedProductImage(@Param("Ids")Collection<String> Ids);
}
