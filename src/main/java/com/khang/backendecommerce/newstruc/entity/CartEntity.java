package com.khang.backendecommerce.newstruc.entity;

import com.khang.backendecommerce.infrastructure.common.entity.abstractentity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name ="tbl_cart")
public class CartEntity extends AbstractEntity<String> implements Serializable {


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;



    @Column(name ="subtotal" , precision = 12 , scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;


    @Column(name = "total_amount" , precision = 12 , scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

        @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private DiscountCustomerEntity discount ;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CartItemEntity> cartItemList = new ArrayList<>();

    public void addCartItem(CartItemEntity cartItem){
        cartItemList.add(cartItem);
        cartItem.setCart(this);
    }
    public void removeCartItem(CartItemEntity cartItem){
        cartItemList.remove(cartItem);
        cartItem.setCart(null);
    }
    //TODO ADD TO DBS
    @Column(name = "delivery_amount")
    private BigDecimal deliveryFee;
}
