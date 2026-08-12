package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import com.khang.backendecommerce.infrastructure.exception.ApplicationErrors;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name ="tbl_inventory")
public class InventoryEntity extends AbstractEntity<String> implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product ;

    @Column(name = "sku")
    private String sku;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private WarehouseEntity warehouse ;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @Column(name = "reserved_quantity ", nullable = false)
    private Integer reservedQuantity;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "inventory_status", columnDefinition = "inventory_status" )
    private InventoryStatus inventoryStatus;

    public  void updateReservedQuantityAndAvailableQuantity(int orderQuantity){
        if(availableQuantity - orderQuantity < 0){
            throw ApplicationErrors.INVENTORY_NOT_ENOUGH;
        }
        availableQuantity -= orderQuantity;
        reservedQuantity+= orderQuantity;
    }
    public void updateQuantityWhenPaymentFailed(int orderQuantity){
        availableQuantity += orderQuantity;
        reservedQuantity -= orderQuantity;
    }
    public void updateQuantityWhenPaymentSuccessOrCOD(int orderQuantity){
        reservedQuantity -=orderQuantity;
    }
    public void updateQuantityWhenSubOrderRejectOrRefund(int orderQuantity){
        availableQuantity +=orderQuantity;
    }

}
