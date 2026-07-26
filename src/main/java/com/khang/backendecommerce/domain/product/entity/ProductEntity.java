package com.khang.backendecommerce.domain.catalog.product.entity;

import com.khang.backendecommerce.domain.catalog.category.entity.CategoryEntity;
import com.khang.backendecommerce.domain.catalog.inventory.entity.InventoryEntity;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.domain.store.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tbl_product")
public class ProductEntity extends AbstractEntity<String> implements Serializable {
    @Column(name = "name")
    private String name ;

    @Column(name = "price")
    private BigDecimal price ;

    @Column(name = "sku")
    private String sku ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity storeId ;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryId ;

    @Column(name = "description")
    private String description;

}
