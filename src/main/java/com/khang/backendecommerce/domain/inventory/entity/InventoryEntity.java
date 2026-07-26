package com.khang.backendecommerce.domain.catalog.inventory.entity;

import com.khang.backendecommerce.domain.catalog.product.entity.ProductEntity;
import com.khang.backendecommerce.domain.catalog.warehouse.entity.WarehouseEntity;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tbl_inventory")
public class InventoryEntity extends AbstractEntity<String> implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity productId ;

    @Column(name = "sku")
    private String sku;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private WarehouseEntity warehouseId ;

    @Column(name = "available_quantity")
    private String quantity;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "inventory_status", columnDefinition = "inventory_status" )
    private InventoryStatus inventoryStatus;

}
