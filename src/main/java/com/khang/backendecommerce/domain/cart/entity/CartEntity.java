package com.khang.backendecommerce.domain.cart.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.domain.delivery.entity.DeliveryFeeEntity;
import com.khang.backendecommerce.domain.user.entity.UserEntity;
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
@Table(name ="tbl_cart")
public class CartEntity extends AbstractEntity<String> implements Serializable {


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_fee_id")
    private DeliveryFeeEntity deliveryFeeEntity;

    @Column(name ="subtotal" , precision = 12 , scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "delivery_amount" , precision = 12 , scale = 2 )
    private BigDecimal deliveryAmount = BigDecimal.ZERO;

    @Column(name = "total_amount" , precision = 12 , scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CartItemEntity> cartItemList = new ArrayList<>();
}
