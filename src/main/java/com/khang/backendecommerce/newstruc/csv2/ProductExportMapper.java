package com.khang.backendecommerce.newstruc.csv2;

import com.khang.backendecommerce.newstruc.entity.ProductEntity;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ProductExportMapper {
    @Select("""
            select p.name, p.price,coalesce(sum(i.available_quantity), 0) AS stockQuantity,
                     case
                        when coalesce(sum(i.available_quantity), 0) = 0 then 'HẾT HÀNG'
                        when coalesce(sum(i.available_quantity), 0) <= #{lowStock} then 'SẮP HẾT HÀNG'
                        else 'CÒN HÀNG'
                    end as stockStatus
                from tbl_product p
                left join tbl_inventory i ON i.product_id = p.id
                group by p.id, p.name, p.sku, p.price
                order by  p.name
        """)
    List<ProductEntity> selectProductForExport(
            @Param("lowStock") int lowStock
    );

}
