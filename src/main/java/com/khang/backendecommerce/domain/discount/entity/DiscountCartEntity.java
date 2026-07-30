package com.khang.backendecommerce.domain.discount.entity;

import com.khang.backendecommerce.domain.cart.entity.CartEntity;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name ="tbl_order")
//public class DiscountCartEntity extends AbstractEntity<String> implements Serializable {
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cart_id")
//    private CartEntity cart ;
//
//    @Column(name ="discount_quantity")
//    private Integer discountQuantity;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "discount_id")
//    private DiscountCustomerEntity discountCustomer ;
//}
