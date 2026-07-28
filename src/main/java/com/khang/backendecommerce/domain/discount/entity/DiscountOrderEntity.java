package com.khang.backendecommerce.domain.discount.entity;

import com.khang.backendecommerce.domain.order.entity.OrderEntity;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
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
@Table(name ="tbl_discount_customer")
public class DiscountOrderEntity extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private DiscountCustomerEntity discountCustomer ;

    @Column(name ="discount_quantity")
    private Integer discountQuantity;

    @Column(name ="discount_value" ,  precision = 12 , scale = 2)
    private BigDecimal discountValue = BigDecimal.ZERO;


}
