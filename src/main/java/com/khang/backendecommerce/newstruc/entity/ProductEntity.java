package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private StoreEntity store ;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category ;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<InventoryEntity> inventoryList = new ArrayList<>();

    @OneToMany(mappedBy = "product" ,fetch = FetchType.LAZY,orphanRemoval = false)
    private List<ProductImageEntity> productImageList = new ArrayList<>();

}
