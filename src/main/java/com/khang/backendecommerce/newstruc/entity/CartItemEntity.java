package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.infrastructure.common.enums.InventoryStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tbl_cart_item")
public class CartItemEntity extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "quantity")
    private Integer quantity;


//    @Column(name = "subtotal")
//    private BigDecimal subtotal;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "inventory_status", columnDefinition = "inventory_status")
    private InventoryStatus inventoryStatus;
//TODO DELETE IN DBS

//     @Column(name = "unite_price")
//    private BigDecimal unitPrice;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "inventory_id")
//    private InventoryEntity inventory;


//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "delivery_fee")
//    private BigDecimal deliveryFee;
}
