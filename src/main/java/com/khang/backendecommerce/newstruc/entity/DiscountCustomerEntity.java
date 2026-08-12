package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder

@Table(name ="tbl_discount_customer")
public class DiscountCustomerEntity extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserEntity customer ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private DiscountEntity discount ;

    @Column(name ="discount_quantity")
    private Integer discountQuantity;

    @Column(name ="discount_value" ,  precision = 12 , scale = 2)
    private BigDecimal discountValue = BigDecimal.ZERO;


}

