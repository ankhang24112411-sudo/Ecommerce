package com.khang.backendecommerce.newstruc.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_product")
public class ProductEntity extends AbstractEntity<String> implements Serializable {
    @ExcelProperty("Tên sản phẩm")
    @Column(name = "name")
    private String name;

    @ExcelProperty("Giá")
    @Column(name = "price")
    private BigDecimal price;

    @ExcelIgnore
    @Column(name = "sku")
    private String sku;

    @ExcelIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @ExcelIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ExcelIgnore
    @Column(name = "description")
    private String description;

    @Transient
    @ExcelProperty("Số lượng")
    private Long stockQuantity;

    @Transient
    @ExcelProperty("Trạng thái")
    private String stockStatus;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<InventoryEntity> inventoryList = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<ProductImageEntity> productImageList = new ArrayList<>();

}
