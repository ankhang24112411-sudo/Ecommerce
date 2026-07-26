package com.khang.backendecommerce.delivery.entity;

import com.khang.backendecommerce.catalog.product.entity.ProductEntity;
import com.khang.backendecommerce.common.entity.abstractentity.AbstractEntity;
import com.khang.backendecommerce.user.dto.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tbl_inventory")
public class DeliveryEntity extends AbstractEntity<String> implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_shipper_id")
    private UserEntity shipperId ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity orderId ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_shipper_id")
    private UserEntity user ;
}
