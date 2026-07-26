package com.khang.backendecommerce.cart.entity;

import com.khang.backendecommerce.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.delivery.entity.DeliveryFee;
import com.khang.backendecommerce.user.dto.UserEntity;
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
@Table(name ="tbl_cart")
public class CartEntity extends AbstractEntity<String> implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_cart_id")
    private String discountCartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private DeliveryFee deliveryFeeId;

    @Column(name ="subtotal")
    private BigDecimal subtotal;

    @Column(name = "delivery_amount")
    private BigDecimal deliveryAmount;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

}
